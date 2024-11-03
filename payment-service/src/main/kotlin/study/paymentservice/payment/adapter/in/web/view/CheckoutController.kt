package study.paymentservice.payment.adapter.`in`.web.view

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono
import study.paymentservice.common.IdempotencyCreator
import study.paymentservice.common.WebAdapter
import study.paymentservice.payment.adapter.`in`.web.request.CheckoutRequest
import study.paymentservice.payment.application.port.`in`.CheckoutCommand
import study.paymentservice.payment.application.port.`in`.CheckoutUseCase

@Controller
@WebAdapter
class CheckoutController(
    private val checkoutUseCase: CheckoutUseCase
) {

    @GetMapping("/")
    fun checkout(request: CheckoutRequest, model: Model): Mono<String> {
        val command = CheckoutCommand(
            cartId = request.cartId,
            buyerId = request.buyerId,
            productIds = request.productIds,
            idempotencyKey = IdempotencyCreator.create(request)
        )

        return checkoutUseCase.checkout(command)
            .map {
                model.addAttribute("orderId", it.orderId)
                model.addAttribute("orderName", it.orderName)
                model.addAttribute("amount", it.amount)
                "checkout"
            }
    }
}
