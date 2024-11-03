package study.paymentservice.payment.adapter.out.persistent.exception

import study.paymentservice.payment.domain.PaymentStatus

class PaymentAlreadyProcessedException (
    val status: PaymentStatus,
    message: String
) : RuntimeException(message)
