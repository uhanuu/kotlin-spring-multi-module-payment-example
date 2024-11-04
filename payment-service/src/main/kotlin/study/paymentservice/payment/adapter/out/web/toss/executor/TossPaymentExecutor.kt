package study.paymentservice.payment.adapter.out.web.toss.executor

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.handler.timeout.TimeoutException
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.retry.Retry
import study.paymentservice.payment.adapter.out.web.toss.exception.PSPConfirmationException
import study.paymentservice.payment.adapter.out.web.toss.exception.TossPaymentError
import study.paymentservice.payment.adapter.out.web.toss.response.TossFailureResponse
import study.paymentservice.payment.adapter.out.web.toss.response.TossPaymentConfirmationResponse
import study.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import study.paymentservice.payment.domain.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class TossPaymentExecutor(
    private val tossPaymentWebClient: WebClient,
    private val objectMapper: ObjectMapper,
    private val uri: String = "/v1/payments/confirm"
) : PaymentExecutor {

    override fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult> {
        return tossPaymentWebClient.post()
            .uri(uri)
            .header("Idempotency-Key", command.orderId)
            .bodyValue("""
                {
                    "paymentKey": "${command.paymentKey}",
                    "orderId": "${command.orderId}",
                    "amount": "${command.amount}"
                }
            """.trimIndent())
            .retrieve()
            .onStatus({ statusCode: HttpStatusCode -> statusCode.is4xxClientError || statusCode.is5xxServerError}) { response ->
                response.bodyToMono(TossFailureResponse::class.java)
                    .flatMap {
                        val error = TossPaymentError.get(it.code)
                        Mono.error<PSPConfirmationException>(
                            PSPConfirmationException(
                                errorCode = error.name,
                                errorMessage = error.description,
                                isSuccess = error.isSuccess(),
                                isFailure = error.isFailure(),
                                isUnknown = error.isUnknown(),
                                isRetryableError = error.isRetryableError()
                            )
                        )
                    }
            }
            .bodyToMono(TossPaymentConfirmationResponse::class.java)
            .map {
                PaymentExecutionResult(
                    paymentKey = command.paymentKey,
                    orderId = command.orderId,
                    extraDetails = PaymentExtraDetails(
                        type = PaymentType.get(it.type),
                        method = PaymentMethod.get(it.method),
                        approvedAt = LocalDateTime.parse(it.approvedAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                        pspRawData = objectMapper.writeValueAsString(it),
                        orderName = it.orderName,
                        pspConfirmationStatus = PSPConfirmationStatus.get(it.status),
                        totalAmount = it.totalAmount.toLong()
                    ),
                    isSuccess = true,
                    isFailure = false,
                    isUnknown = false,
                    isRetryable = false
                )
            }
            // 간단하게 retry 로직 구현 가능하며 재시도 과정에서 변동성을 주기 위해(트래픽 몰리는 문제 때문에) jitter 적용
            .retryWhen(Retry.backoff(2, Duration.ofSeconds(1)).jitter(0.1)
                .filter { (it is PSPConfirmationException && it.isRetryableError) || it is TimeoutException }
//                .doBeforeRetry { println("""
//                    before retry hook: retryCont: ${it.totalRetries()},
//                    errorCode: ${(it.failure() as PSPConfirmationException).errorCode}
//                    isUnknown: ${(it.failure() as PSPConfirmationException).isUnknown}
//                    isFailure: ${(it.failure() as PSPConfirmationException).isFailure}
//                """.trimIndent()) }
                .onRetryExhaustedThrow { _, retrySignal -> retrySignal.failure() }
            )
    }
}
