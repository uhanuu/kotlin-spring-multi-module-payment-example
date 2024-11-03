package study.paymentservice.payment.domain

/**
 * 결제 방법을 나타내기 위해 사용
 *
 * 카드결제나 간편결제 같은 상태를 구분하는데 사용됩니다.
 */
enum class PaymentMethod(description: String) {
    EASY_PAY("간편결제")
}
