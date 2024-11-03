package study.paymentservice.payment.adapter.out.persistent

import reactor.core.publisher.Mono
import study.paymentservice.common.PersistentAdapter
import study.paymentservice.payment.adapter.out.persistent.repository.PaymentRepository
import study.paymentservice.payment.adapter.out.persistent.repository.PaymentStatusUpdateRepository
import study.paymentservice.payment.adapter.out.persistent.repository.PaymentValidationRepository
import study.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand
import study.paymentservice.payment.application.port.out.PaymentStatusUpdatePort
import study.paymentservice.payment.application.port.out.PaymentValidationPort
import study.paymentservice.payment.application.port.out.SavePaymentPort
import study.paymentservice.payment.domain.PaymentEvent

@PersistentAdapter
class PaymentPersistentAdapter (
    private val paymentRepository: PaymentRepository,
    private val paymentStatusUpdateRepository: PaymentStatusUpdateRepository,
    private val paymentValidationRepository: PaymentValidationRepository
) : SavePaymentPort, PaymentStatusUpdatePort, PaymentValidationPort {

    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return paymentRepository.save(paymentEvent)
    }

    override fun updatePaymentStatusToExecuting(orderId: String, paymentKey: String): Mono<Boolean> {
        return paymentStatusUpdateRepository.updatePaymentStatusToExecuting(orderId, paymentKey)
    }

    override fun isValid(orderId: String, amount: Long): Mono<Boolean> {
        return paymentValidationRepository.isValid(orderId, amount)
    }

    override fun updatePaymentStatus(command: PaymentStatusUpdateCommand): Mono<Boolean> {
        return paymentStatusUpdateRepository.updatePaymentStatus(command)
    }
}

