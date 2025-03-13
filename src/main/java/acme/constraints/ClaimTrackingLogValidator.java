
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.datatypes.ClaimLogStatus;
import acme.entities.claimLog.ClaimTrackingLog;

@Validator
public class ClaimTrackingLogValidator extends AbstractValidator<ValidClaimTrackingLog, ClaimTrackingLog> {

	@Override
	protected void initialise(final ValidClaimTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ClaimTrackingLog value, final ConstraintValidatorContext context) {
		if (!value.getResolutionPercentage().equals(100.0)) {
			if (!value.getStatus().equals(ClaimLogStatus.PENDING))
				super.state(context, false, "state", "state must be pending if resolution percentage is not 100.0");
			if (value.getResolutionDescription() != null)
				super.state(context, false, "description", "description must be null if resolution percentage is not 100.0");
		}
		return !super.hasErrors(context);
	}

}
