package study.paymentservice.payment.domain

/**
 * 결제 유형을 분류하기 위해 사용
 *
 * 토스페이먼트에서는 자동결제나 브랜드페이와 같은 다른 결제 유형도 존재한다.
 */
enum class PaymentType(description: String) {
    NORMAL("일반결제");

    companion object {
        fun get(type: String): PaymentType {
            return entries.find { it.name == type } ?: error("PaymentType (type:$type) 은 올바르지 않은 결제 타입입니다.")
        }
    }
}
