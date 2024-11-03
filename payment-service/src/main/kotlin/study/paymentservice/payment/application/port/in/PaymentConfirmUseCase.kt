package study.paymentservice.payment.application.port.`in`

import reactor.core.publisher.Mono
import study.paymentservice.payment.domain.PaymentConfirmationResult

interface PaymentConfirmUseCase {

    fun confirm(command: PaymentConfirmCommand): Mono<PaymentConfirmationResult>
}
