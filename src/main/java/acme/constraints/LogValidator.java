
package acme.constraints;

import java.time.Instant;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import acme.client.components.datatypes.Moment;
import acme.entities.Log;

@Component
public class LogValidator implements ConstraintValidator<ValidLog, Log> {

	@Autowired
	public LogValidator() {
	}

	@Override
	public boolean isValid(final Log log, final ConstraintValidatorContext context) {
		// comprobamos que no sea válido ni futuro
		Moment registrationMoment = log.getRegistrationMoment();
		if (registrationMoment == null || registrationMoment.after(Date.from(Instant.now()))) {
			context.buildConstraintViolationWithTemplate("Registration date not valid: null or future").addConstraintViolation();
			return false;
		}

		// comprobamos que el momento del registro es tras la finalización del vuelo
		Date scheduledArrival = log.getLeg().getScheduledArrival();
		if (registrationMoment.before(scheduledArrival)) {
			context.buildConstraintViolationWithTemplate("Registration date not valid: before the scheduled arrival of the flight").addConstraintViolation();
			return false;
		}

		// TODO Auto-generated method stub
		return false;
	}

}
