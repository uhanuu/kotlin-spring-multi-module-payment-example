package study.paymentservice.payment.application.service

import io.netty.handler.timeout.TimeoutException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import study.paymentservice.payment.adapter.out.persistent.exception.PaymentAlreadyProcessedException
import study.paymentservice.payment.adapter.out.persistent.exception.PaymentValidationException
import study.paymentservice.payment.adapter.out.web.toss.exception.PSPConfirmationException
import study.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import study.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand
import study.paymentservice.payment.application.port.out.PaymentStatusUpdatePort
import study.paymentservice.payment.domain.PaymentConfirmationResult
import study.paymentservice.payment.domain.PaymentFailure
import study.paymentservice.payment.domain.PaymentStatus

@Service
class PaymentErrorHandler (
   private val paymentStatusUpdatePort: PaymentStatusUpdatePort
) {

    fun handlePaymentConfirmationError(
        error: Throwable,
        command: PaymentConfirmCommand
    ): Mono<PaymentConfirmationResult> {
        val (status, failure) = when (error) {
            is PSPConfirmationException -> Pair(error.paymentStatus(), PaymentFailure(error.errorCode, error.errorMessage))
            is PaymentValidationException -> Pair(PaymentStatus.FAILURE, PaymentFailure(error::class.simpleName ?: "", error.message ?: ""))
            is PaymentAlreadyProcessedException -> return Mono.just(PaymentConfirmationResult(status = error.status, failure = PaymentFailure(message = error.message ?: "", errorCode = error::class.simpleName ?: "")))
            is TimeoutException -> Pair(PaymentStatus.UNKNOWN, PaymentFailure(error::class.simpleName ?: "", error.message ?: ""))
            else -> Pair(PaymentStatus.UNKNOWN, PaymentFailure(error::class.simpleName ?: "", error.message ?: ""))
        }

        val paymentStatusUpdateCommand = PaymentStatusUpdateCommand(
            paymentKey = command.paymentKey,
            orderId = command.orderId,
            status = status,
            failure = failure
        )

        return paymentStatusUpdatePort.updatePaymentStatus(paymentStatusUpdateCommand)
            .map { PaymentConfirmationResult(status, failure) }
    }
}