
package acme.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented

@Constraint(validatedBy = {})
@ReportAsSingleViolation

@NotBlank
@Pattern(regexp = "^[A-Z0-9]{6,9}$", message = "El número de pasaporte debe tener entre 6 y 9 caracteres alfanuméricos en mayúscula")

public @interface ValidPassport {

	// Standard validation properties -----------------------------------------

	String message() default "{javax.validation.constraints.Email.message}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
