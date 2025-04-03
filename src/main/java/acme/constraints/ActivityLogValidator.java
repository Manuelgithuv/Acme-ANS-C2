
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.activityLog.ActivityLog;

@Validator
public class ActivityLogValidator extends AbstractValidator<ValidActivityLog, ActivityLog> {

	@Override
	protected void initialise(final ValidActivityLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ActivityLog log, final ConstraintValidatorContext context) {
		Boolean result;

		// comprobamos que el log está registrado al tramo de vuelo adecuado
		result = log.getFlightAssignment().getLeg().equals(log.getLeg())
			// y que se haya registrado después del aterrizaje
			&& log.getRegistrationMoment().after(log.getLeg().getScheduledArrival());

		return result;
	}

}
