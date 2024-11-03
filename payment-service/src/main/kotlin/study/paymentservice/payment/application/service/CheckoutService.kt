package study.paymentservice.payment.application.service

import reactor.core.publisher.Mono
import study.paymentservice.common.UseCase
import study.paymentservice.payment.application.port.`in`.CheckoutCommand
import study.paymentservice.payment.application.port.`in`.CheckoutUseCase
import study.paymentservice.payment.application.port.out.LoadProductPort
import study.paymentservice.payment.application.port.out.SavePaymentPort
import study.paymentservice.payment.domain.*

@UseCase
class CheckoutService(
    private val loadProductPort: LoadProductPort,
    private val savePaymentPort: SavePaymentPort
) : CheckoutUseCase {

    override fun checkout(command: CheckoutCommand): Mono<CheckoutResult> {
        return loadProductPort.getProducts(command.cartId, command.productIds)
            .collectList()
            .map { createPaymentEvent(command, it) }
            .flatMap { savePaymentPort.save(it).thenReturn(it) }
            .map { CheckoutResult(amount = it.totalAmount(), orderId = it.orderId, orderName = it.orderName) }
    }

    private fun createPaymentEvent(command: CheckoutCommand, products: List<Product>): PaymentEvent {
        return PaymentEvent(
            buyerId = command.buyerId,
            orderId = command.idempotencyKey,
            orderName = products.joinToString { it.name },
            paymentOrders = products.map {
                PaymentOrder(
                    sellerId = it.sellerId,
                    orderId = command.idempotencyKey,
                    productId = it.id,
                    amount = it.amount,
                    paymentStatus = PaymentStatus.NOT_STARTED,
                )
            }
        )
    }
}
