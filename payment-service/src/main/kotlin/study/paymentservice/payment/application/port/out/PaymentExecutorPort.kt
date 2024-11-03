package study.paymentservice.payment.application.port.out

import reactor.core.publisher.Mono
import study.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import study.paymentservice.payment.domain.PaymentExecutionResult

interface PaymentExecutorPort {

    fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult>
}
