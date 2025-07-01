
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
					super.state(context, false, "resolutionPercentage", "assistance-agent.claim-log.validation.message.resolutionPercentage.must-be-pending");
				if (value.getResolutionDescription() != null && value.getResolutionDescription() != "")
					super.state(context, false, "resolutionDescription", "assistance-agent.claim-log.validation.message.resolutionDescription.must-be-empty");
				if (value.getCompensation() != null)
					super.state(context, false, "compensation", "assistance-agent.claim-log.validation.message.compensation.must-be-null");
			} else {
				if (value.getStatus() != null && value.getStatus().equals(ClaimStatus.PENDING))
					super.state(context, false, "resolutionPercentage", "assistance-agent.claim-log.validation.message.resolutionPercentage.accepted-or-rejected");
				if (value.getResolutionDescription() == null || value.getResolutionDescription().equals(""))
					super.state(context, false, "resolutionDescription", "assistance-agent.claim-log.validation.message.resolutionDescription.must-not-be-empty");
			}

			if (value.getLastUpdateMoment() != null && value.getClaim().getRegistrationMoment().after(value.getLastUpdateMoment()))
				super.state(context, false, "lastUpdateMoment", "assistance-agent.claim-log.validation.message.lastUpdateMoment.must-be-after");

			if (value.getCreationMoment() != null && value.getClaim().getRegistrationMoment().after(value.getCreationMoment()))
				super.state(context, false, "creationMoment", "assistance-agent.claim-log.validation.message.creationMoment.after-registration");

			if (value.getCreationMoment() != null && value.getLastUpdateMoment() != null && value.getLastUpdateMoment().before(value.getCreationMoment()))
				super.state(context, false, "lastUpdateMoment", "assistance-agent.claim-log.validation.message.lastUpdateMoment.cant-be-before");

		}
		return !super.hasErrors(context);
	}

}
