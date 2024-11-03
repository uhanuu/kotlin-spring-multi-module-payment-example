package study.paymentservice.payment.adapter.`in`.web.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import study.paymentservice.common.WebAdapter
import study.paymentservice.payment.adapter.`in`.web.request.TossPaymentConfirmRequest
import study.paymentservice.payment.adapter.`in`.web.response.ApiResponse
import study.paymentservice.payment.adapter.out.web.executor.TossPaymentExecutor

@WebAdapter
@RequestMapping("/v1/toss")
@RestController
class TossPaymentController(
    private val tossPaymentExecutor: TossPaymentExecutor
) {

    @PostMapping("/confirm")
    fun confirm(@RequestBody request: TossPaymentConfirmRequest): Mono<ResponseEntity<ApiResponse<String>>> {
        return tossPaymentExecutor.executor(
            paymentKey = request.paymentKey,
            orderId = request.orderId,
            amount = request.amount.toString()
        ).map {
            ResponseEntity.ok().body(
                ApiResponse.with(HttpStatus.OK, "ok", it)
            )
        }
    }
}
