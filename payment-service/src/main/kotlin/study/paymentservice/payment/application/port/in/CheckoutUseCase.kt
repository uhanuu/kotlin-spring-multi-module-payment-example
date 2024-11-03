package study.paymentservice.payment.application.port.`in`

import reactor.core.publisher.Mono
import study.paymentservice.payment.domain.CheckoutResult

interface CheckoutUseCase {

    fun checkout(command: CheckoutCommend): Mono<CheckoutResult>
}
