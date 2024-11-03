package study.paymentservice.payment.adapter.out.web.product

import reactor.core.publisher.Flux
import study.paymentservice.common.WebAdapter
import study.paymentservice.payment.adapter.out.web.product.client.ProductClient
import study.paymentservice.payment.application.port.out.LoadProductPort
import study.paymentservice.payment.domain.Product

@WebAdapter
class ProductWebAdapter(
    private val productClient: ProductClient
) : LoadProductPort {
    override fun getProducts(cartId: Long, productIds: List<Long>): Flux<Product> {
        return productClient.getProducts(cartId, productIds)
    }
}
