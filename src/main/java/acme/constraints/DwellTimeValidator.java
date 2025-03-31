
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.service.Service;

@Validator
public class DwellTimeValidator extends AbstractValidator<ValidServiceDwellTime, Service> {

	@Override
	protected void initialise(final ValidServiceDwellTime annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service value, final ConstraintValidatorContext context) {
		if (value.getAverageDwellTime() == null || value.getAverageDwellTime() <= 0.0)
			super.state(context, false, "hours", "the.hours.of.the.dwell-time.must.be.positive");
		return !super.hasErrors(context);
	}

}
