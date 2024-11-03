package study.paymentservice.payment.adapter.out.persistent.repository

import reactor.core.publisher.Mono
import study.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand

interface PaymentStatusUpdateRepository {
    fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean>

    fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean>
}
