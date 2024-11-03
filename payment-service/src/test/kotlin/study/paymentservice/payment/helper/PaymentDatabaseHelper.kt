package study.paymentservice.payment.helper

import reactor.core.publisher.Mono
import study.paymentservice.payment.domain.PaymentEvent

interface PaymentDatabaseHelper {

    fun getPayments(orderId: String): PaymentEvent?
    fun clean(): Mono<Void>
}
