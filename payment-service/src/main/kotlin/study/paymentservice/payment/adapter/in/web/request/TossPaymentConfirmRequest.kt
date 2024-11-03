package study.paymentservice.payment.adapter.`in`.web.request

data class TossPaymentConfirmRequest(
    val paymentKey: String,
    var orderId: String,
    var amount: Long
)

