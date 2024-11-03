package study.paymentservice.payment.application.port.out

import reactor.core.publisher.Flux
import study.paymentservice.payment.domain.Product

interface LoadProductPort {
    fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product>
}
