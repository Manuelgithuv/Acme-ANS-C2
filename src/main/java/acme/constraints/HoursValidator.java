
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class HoursValidator extends AbstractValidator<ValidHours, Double> {

	@Override
	protected void initialise(final ValidHours annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Double value, final ConstraintValidatorContext context) {
		if (value == null || value <= 0.0)
			super.state(context, false, "Hours", "The hours of a leg must be positive");
		return !super.hasErrors(context);
	}

}
