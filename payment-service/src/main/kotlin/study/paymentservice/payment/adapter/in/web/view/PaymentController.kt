package study.paymentservice.payment.adapter.`in`.web.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import study.paymentservice.common.WebAdapter

@Controller
@WebAdapter
@RequestMapping("/v1/toss")
class PaymentController {

    @GetMapping("/success")
    fun successPage(): Mono<String> {
        return Mono.just("success")
    }

    @GetMapping("/fail")
    fun failPage(): Mono<String> {
        return Mono.just("fail")
    }
}
