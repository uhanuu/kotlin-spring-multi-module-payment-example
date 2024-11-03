package study.paymentservice.payment.adapter.out.persistent.repository

import reactor.core.publisher.Mono
import study.paymentservice.payment.domain.PaymentEvent

interface PaymentRepository {

    fun save(paymentEvent: PaymentEvent): Mono<Void>
}
