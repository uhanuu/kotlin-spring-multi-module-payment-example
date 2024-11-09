package study.paymentservice.payment.application.port.out

import reactor.core.publisher.Flux
import study.paymentservice.payment.domain.PendingPaymentEvent

interface LoadPendingPaymentPort {

    fun getPendingPayments(): Flux<PendingPaymentEvent>
}
