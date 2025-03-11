
package acme.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@Constraint(validatedBy = {})
@ReportAsSingleViolation

@Pattern(regexp = "^\\d{4}$", message = "Los últimos 4 dígitos de la tarjeta deben ser un número de 4 dígitos")

public @interface ValidLastCardNibble {

	// Standard validation properties -----------------------------------------

	String message() default "{javax.validation.constraints.Email.message}";
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
