package study.paymentservice.payment.adapter.out.persistent

import reactor.core.publisher.Mono
import study.paymentservice.common.PersistentAdapter
import study.paymentservice.payment.adapter.out.persistent.repository.PaymentRepository
import study.paymentservice.payment.application.port.out.SavePaymentPort
import study.paymentservice.payment.domain.PaymentEvent

@PersistentAdapter
class PaymentPersistentAdapter (
    private val paymentRepository: PaymentRepository
) : SavePaymentPort {

    override fun save(paymentEvent: PaymentEvent): Mono<Void> {
        return paymentRepository.save(paymentEvent)
    }
}

