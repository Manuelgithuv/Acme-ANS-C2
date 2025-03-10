
package acme.constraints;

import java.time.Instant;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import acme.entities.log.Log;

@Component
public class LogValidator implements ConstraintValidator<ValidLog, Log> {

	@Autowired
	public LogValidator() {
	}

	@Override
	public boolean isValid(final Log log, final ConstraintValidatorContext context) {
		// comprobamos que no sea válido ni futuro
		Date registrationMoment = log.getRegistrationMoment();
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

		// comprobamos que el assignment fuese a la leg del log
		if (log.getLeg() != log.getFlightAssignment().getLeg()) {
			context.buildConstraintViolationWithTemplate("Registration date not valid: the assignment was not to the specified leg").addConstraintViolation();
			return false;
		}

		return true;
	}

}
