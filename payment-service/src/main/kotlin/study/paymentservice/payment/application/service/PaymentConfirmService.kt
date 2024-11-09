package study.paymentservice.payment.application.service

import reactor.core.publisher.Mono
import study.paymentservice.common.UseCase
import study.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import study.paymentservice.payment.application.port.`in`.PaymentConfirmUseCase
import study.paymentservice.payment.application.port.out.PaymentExecutorPort
import study.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand
import study.paymentservice.payment.application.port.out.PaymentStatusUpdatePort
import study.paymentservice.payment.application.port.out.PaymentValidationPort
import study.paymentservice.payment.domain.PaymentConfirmationResult

@UseCase
class PaymentConfirmService(
    private val paymentStatusUpdatePort: PaymentStatusUpdatePort,
    private val paymentValidationPort: PaymentValidationPort,
    private val paymentExecutorPort: PaymentExecutorPort,
    private val paymentErrorHandler: PaymentErrorHandler
) : PaymentConfirmUseCase {

    override fun confirm(command: PaymentConfirmCommand): Mono<PaymentConfirmationResult> {
        return paymentStatusUpdatePort.updatePaymentStatusToExecuting(command.orderId, command.paymentKey)
            .filterWhen { paymentValidationPort.isValid(command.orderId, command.amount) }
            .flatMap { paymentExecutorPort.execute(command) }
            .flatMap {
                paymentStatusUpdatePort.updatePaymentStatus(
                    command = PaymentStatusUpdateCommand(
                        paymentKey = it.paymentKey,
                        orderId = it.orderId,
                        status = it.paymentStatus(),
                        extraDetails = it.extraDetails,
                        failure = it.failure
                    )
                ).thenReturn(it)
            }
            .map { PaymentConfirmationResult(status = it.paymentStatus(), failure = it.failure) }
            .onErrorResume { paymentErrorHandler.handlePaymentConfirmationError(it, command) }
    }

}
