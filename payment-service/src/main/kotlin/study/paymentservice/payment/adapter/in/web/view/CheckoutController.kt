package study.paymentservice.payment.adapter.`in`.web.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import reactor.core.publisher.Mono
import study.paymentservice.common.WebAdapter

@Controller
@WebAdapter
class CheckoutController {

    @GetMapping("/")
    fun checkout(): Mono<String> {
        return Mono.just("checkout")
    }
}
