package study.paymentservice.payment.adapter.out.web.toss

import reactor.core.publisher.Mono
import study.paymentservice.common.WebAdapter
import study.paymentservice.payment.adapter.out.web.toss.executor.PaymentExecutor
import study.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import study.paymentservice.payment.application.port.out.PaymentExecutorPort
import study.paymentservice.payment.domain.PaymentExecutionResult

@WebAdapter
class PaymentExecutorWebAdapter (
    private val paymentExecutor: PaymentExecutor
) : PaymentExecutorPort {
    override fun execute(command: PaymentConfirmCommand): Mono<PaymentExecutionResult> {
        return paymentExecutor.executor(command)
    }
}

