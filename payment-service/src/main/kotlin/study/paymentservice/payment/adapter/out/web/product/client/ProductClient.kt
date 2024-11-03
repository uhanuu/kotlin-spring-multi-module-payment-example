package study.paymentservice.payment.adapter.out.web.product.client

import reactor.core.publisher.Flux
import study.paymentservice.payment.domain.Product

interface ProductClient {

    fun getProducts(cartId: Long, productsIds: List<Long>): Flux<Product>
}
