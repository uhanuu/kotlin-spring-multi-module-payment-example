package study.paymentservice.payment.domain

data class PaymentFailure (
  val errorCode: String,
  val message: String
)
