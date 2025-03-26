
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.leg.Leg;

@Validator
public class HoursValidator extends AbstractValidator<ValidHours, Leg> {

	@Override
	protected void initialise(final ValidHours annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg value, final ConstraintValidatorContext context) {
		if (value.getHours() == null || value.getHours() <= 0.0)
			super.state(context, false, "hours", "the.hours.of.a.leg.must.be.positive");
		return !super.hasErrors(context);
	}

}
