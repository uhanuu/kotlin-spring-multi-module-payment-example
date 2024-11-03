package study.paymentservice.common;

import kotlin.annotation.AnnotationTarget;
import kotlin.annotation.Target;
import org.springframework.stereotype.Component;

@Target(allowedTargets = AnnotationTarget.CLASS)
@Component
public @interface WebAdapter {
}
