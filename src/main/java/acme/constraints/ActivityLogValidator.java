
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.entities.activity_log.ActivityLog;

public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog log, final ConstraintValidatorContext context) {
		boolean result;

		result = log.getFlightAssignment().getLeg().equals(log.getLeg()) // comprobamos que el log está registrado al tramo de vuelo adecuado
			&& log.getRegistrationMoment().after(log.getLeg().getScheduledArrival()); // y que se haya registrado después del aterrizaje

		return result;
	}

}
