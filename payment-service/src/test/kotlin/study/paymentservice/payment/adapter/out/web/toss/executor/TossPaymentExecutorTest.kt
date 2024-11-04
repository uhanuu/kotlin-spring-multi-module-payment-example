package study.paymentservice.payment.adapter.out.web.toss.executor

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import study.paymentservice.payment.adapter.out.web.toss.exception.PSPConfirmationException
import study.paymentservice.payment.adapter.out.web.toss.exception.TossPaymentError
import study.paymentservice.payment.application.port.`in`.PaymentConfirmCommand
import study.paymentservice.payment.helper.PSPTestWebClientConfiguration
import java.util.*


/**
 * 실제 요청을 보내므로 실행 시간이 오래 걸리는 테스트.
 *
 * 자동화된 테스트 실행에서 제외하려면 `@Tag` 어노테이션을 활용할 수 있다.
 *
 * 예시:
 * ```
 * @Tag("TooLongTime")
 * ```
 *
 * 아래와 같이 `build.gradle`에서 설정하여 특정 태그의 테스트를 제외할 수 있다:
 * ```
 * tasks.withType<Test> {
 *   useJUnitPlatform {
 *     excludeTags("TooLongTime")
 *   }
 * }
 * ```
 */
@SpringBootTest
@Import(PSPTestWebClientConfiguration::class)
@Tag("TooLongTime")
class TossPaymentExecutorTest (
    @Autowired private val pspTestWebClientConfiguration: PSPTestWebClientConfiguration,
    @Autowired private val objectMapper: ObjectMapper
){

    @Test
    fun `should handle correctly various TossPaymentError scenarios`() {
        generateErrorScenarios().forEach { errorScenario ->
            val command = PaymentConfirmCommand(
                paymentKey = UUID.randomUUID().toString(),
                orderId = UUID.randomUUID().toString(),
                amount = 10000L
            )

            val paymentExecutor = TossPaymentExecutor(
                tossPaymentWebClient = pspTestWebClientConfiguration.createTestTossWebClient(
                    Pair("TossPayments-Test-Code", errorScenario.errorCode)
                ),
                objectMapper = objectMapper,
                uri = "/v1/payments/key-in"
            )

            // catchThrowable 로 처리하는게 더 좋지 않을까?
            try {
                paymentExecutor.execute(command).block()
            } catch (e: PSPConfirmationException) {
                assertThat(e.isSuccess).isEqualTo(errorScenario.isSuccess)
                assertThat(e.isFailure).isEqualTo(errorScenario.isFailure)
                assertThat(e.isUnknown).isEqualTo(errorScenario.isUnknown)
            }
        }
    }

    private fun generateErrorScenarios(): List<ErrorScenario> {
        return TossPaymentError.entries.map { error ->
            ErrorScenario(
                errorCode = error.name,
                isSuccess = error.isSuccess(),
                isFailure = error.isFailure(),
                isUnknown = error.isUnknown()
            )
        }
    }
}

data class ErrorScenario(
    val errorCode: String,
    val isFailure: Boolean,
    val isUnknown: Boolean,
    val isSuccess: Boolean
)
