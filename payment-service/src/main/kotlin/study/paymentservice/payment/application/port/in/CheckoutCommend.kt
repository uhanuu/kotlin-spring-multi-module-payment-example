package study.paymentservice.payment.application.port.`in`

data class CheckoutCommend(
    val cartId: Long,
    val buyerId: Long,
    val productIds: List<Long>,
    val idempotencyKey: String
)
