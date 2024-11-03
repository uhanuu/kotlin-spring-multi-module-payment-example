package study.paymentservice.payment.domain

data class PaymentExecutionFailure (
  val errorCode: String,
  val message: String
)
