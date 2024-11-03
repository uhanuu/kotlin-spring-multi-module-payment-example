package study.paymentservice.payment.domain

/**
 * 결제 방법을 나타내기 위해 사용
 *
 * 카드결제나 간편결제 같은 상태를 구분하는데 사용됩니다.
 */
enum class PaymentMethod(val method: String) {
    EASY_PAY("간편결제");

    companion object {
        fun get(method: String): PaymentMethod {
            return entries.find { it.method == method } ?: error("Payment Method (method: $method) 는 올바르지 않은 결제 방법입니다. ")
        }
    }
}
