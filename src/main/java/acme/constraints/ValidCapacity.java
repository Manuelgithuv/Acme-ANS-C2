
package acme.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Min;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation

@Min(1)
public @interface ValidCapacity {
	// Standard validation properties -----------------------------------------

	String message() default "Invalid cargo weight";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
