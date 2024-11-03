package study.paymentservice.payment.adapter.out.web.product.client

import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import study.paymentservice.payment.domain.Product
import java.math.BigDecimal

@Component
class MockProductClient : ProductClient {

    override fun getProducts(cartId: Long, productsIds: List<Long>): Flux<Product> {
        return Flux.fromIterable(
            productsIds.map {
                Product(
                    id = it,
                    amount = BigDecimal(it * 10000),
                    quantity = 2,
                    name = "test_product_$it",
                    sellerId = 1
                )
            }
        )
    }
}
