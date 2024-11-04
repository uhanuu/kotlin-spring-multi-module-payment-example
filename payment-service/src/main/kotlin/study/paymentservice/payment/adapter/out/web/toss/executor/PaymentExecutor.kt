package study.paymentservice.payment.adapter.out.web.toss.executor

import reactor.core.publisher.Mono
import study.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import study.paymentservice.payment.domain.PaymentExecutionResult

interface PaymentExecutor {

    fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult>
}
