package study.paymentservice.payment.adapter.out.persistent.repository

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import study.paymentservice.payment.domain.PaymentEvent
import study.paymentservice.payment.domain.PendingPaymentEvent

interface PaymentRepository {

    fun save(paymentEvent: PaymentEvent): Mono<Void>

    fun getPendingPayments(): Flux<PendingPaymentEvent>
}
