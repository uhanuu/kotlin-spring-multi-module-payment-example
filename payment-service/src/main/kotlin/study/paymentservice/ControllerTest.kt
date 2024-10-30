package study.paymentservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import study.paycore.logger

@RestController
@RequestMapping("/")
class ControllerTest {

    @GetMapping
    fun getCount(): String {
        logger.info { "hello world" }
        return "HelloWorld"
    }
}