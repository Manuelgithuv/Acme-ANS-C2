
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.datatypes.ClaimStatus;
import acme.entities.claimLog.ClaimTrackingLog;

@Validator
public class ClaimTrackingLogValidator extends AbstractValidator<ValidClaimTrackingLog, ClaimTrackingLog> {

	@Override
	protected void initialise(final ValidClaimTrackingLog annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final ClaimTrackingLog value, final ConstraintValidatorContext context) {
		if (value.getClaim() != null) {
			if (value.getResolutionPercentage() != null && !value.getResolutionPercentage().equals(100.0)) {
				if (value.getStatus() != null && !value.getStatus().equals(ClaimStatus.PENDING))
					super.state(context, false, "resolutionPercentage", "state of associated claim must be pending if resolution percentage is not 100.0");
				if (value.getResolutionDescription() != null && value.getResolutionDescription() != "")
					super.state(context, false, "resolutionDescription", "description must be null or empty if resolution percentage is not 100.0");
				if (value.getCompensation() != null)
					super.state(context, false, "compensation", "Compensation must be null or empty if resolution percentage is not 100.0");
			} else if (value.getStatus() != null && value.getStatus().equals(ClaimStatus.PENDING))
				super.state(context, false, "resolutionPercentage", "state of claim must be accepted or rejected if resolution percentage is 100.0");

			if (value.getLastUpdateMoment() != null && value.getClaim().getRegistrationMoment().after(value.getLastUpdateMoment()))
				super.state(context, false, "lastUpdateMoment", "LastUpdateMoment must be after the associated claim registration moment");
			if (value.getClaim().getLastTrackingLog() != null) {
				if (value.getLastUpdateMoment() != null && value.getClaim().getLastTrackingLog().getLastUpdateMoment().after(value.getLastUpdateMoment()))
					super.state(context, false, "lastUpdateMoment", "LastUpdateMoment must be after the last tracking log of the associated claim");
				if (value.getResolutionPercentage() != null && value.getResolutionPercentage() < value.getClaim().getLastTrackingLog().getResolutionPercentage())
					super.state(context, false, "resolutionPercentage", "ResolutionPercentage must be equal or greater than the ResolutionPercentage of the last tracking log of the associated claim");
			}
		}
		return !super.hasErrors(context);
	}

}
