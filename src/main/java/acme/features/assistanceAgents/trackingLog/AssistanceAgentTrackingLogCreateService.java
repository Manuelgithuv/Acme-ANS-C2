
package acme.features.assistanceAgents.trackingLog;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.entities.claim.Claim;
import acme.entities.claimLog.ClaimTrackingLog;
import acme.features.assistanceAgents.claim.AssistanceAgentClaimRepository;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateService extends AbstractGuiService<AssistanceAgent, ClaimTrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository	repository;

	@Autowired
	private AssistanceAgentClaimRepository			claimRepository;


	@Override
	public void authorise() {

		boolean status;

		AssistanceAgent agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean claimCheck = true;

		if (!super.getRequest().getMethod().equals("GET")) {
			int claimId = super.getRequest().hasData("claim") ? super.getRequest().getData("claim", int.class) : 0;
			Claim claim = this.claimRepository.findClaimById(claimId);
			if (claimId == 0)
				claimCheck = true;
			else if (claim != null && claim.getAssistanceAgent().getId() == agentId)
				claimCheck = true;
			else
				claimCheck = false;
		}

		status = agent.getClass().equals(AssistanceAgent.class) && claimCheck;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ClaimTrackingLog claimLog;
		claimLog = new ClaimTrackingLog();
		claimLog.setPublished(false);
		claimLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		claimLog.setCreationMoment(MomentHelper.getCurrentMoment());
		if (!super.getRequest().getMethod().equals("GET")) {
			int claimId = super.getRequest().getData("claim", int.class);
			Claim claim = this.claimRepository.findClaimById(claimId);
			claimLog.setClaim(claim);
		}
		super.getBuffer().addData(claimLog);
	}

	@Override
	public void bind(final ClaimTrackingLog claimLog) {
		String money = super.getRequest().getData("compensation", String.class);
		if (money == "") {
			claimLog.setCompensation(null);
			super.bindObject(claimLog, "stepUndergoing", "resolutionPercentage", "resolutionDescription", "status");
		} else
			super.bindObject(claimLog, "stepUndergoing", "resolutionPercentage", "resolutionDescription", "compensation", "status");
	}

	@Override
	public void validate(final ClaimTrackingLog claimLog) {

		if (claimLog.getClaim() == null)
			super.state(false, "claim", "assistance-agent.claim-tracking-log.create.claim");
		ClaimTrackingLog value = claimLog;
		if (value.getClaim() != null && value.getClaim().getLastTrackingLog() != null)
			if (value.getResolutionPercentage() != null && value.getResolutionPercentage() < value.getClaim().getLastTrackingLog().getResolutionPercentage())
				super.state(false, "resolutionPercentage", "assistance-agent.claim-log.validation.message.resolutionPercentage.equal-or-greater");
		if (value.getClaim() != null && value.getClaim().getLastTrackingLog() != null)
			if (value.getCreationMoment() != null && value.getClaim().getLastTrackingLog().getCreationMoment().after(value.getCreationMoment()))
				super.state(false, "creationMoment", "assistance-agent.claim-log.validation.message.creationMoment.cant-be-before");
		if (claimLog.getClaim() != null && claimLog.getResolutionPercentage() != null && claimLog.getResolutionPercentage().equals(100.0)) {
			List<ClaimTrackingLog> ls = this.repository.findAllByClaimIdOrderByCreationMomentDescIdDesc(claimLog.getClaim().getId());
			List<ClaimTrackingLog> filtered = ls.stream().filter(z -> z.getResolutionPercentage().equals(100.0)).toList();
			if (filtered.size() > 1) {
				ClaimTrackingLog secondLastCompletedLog = filtered.get(1);
				if (secondLastCompletedLog.getId() != claimLog.getId() && secondLastCompletedLog.isPublished())
					super.state(false, "claim", "assistance-agent.claim-tracking-log.create.claim.lastLogError");
			}
		}
		if (claimLog.getClaim() != null && claimLog.getClaim().getLastTrackingLog() != null && claimLog.getClaim().isPublished() && claimLog.getClaim().getLastTrackingLog().isPublished()
			&& claimLog.getClaim().getLastTrackingLog().getResolutionPercentage().equals(100.0))
			if (!claimLog.getClaim().getLastTrackingLog().getStatus().equals(claimLog.getStatus()))
				super.state(false, "status", "assistance-agent.claim-tracking-log.create.status.review-must-match");

	}

	@Override
	public void perform(final ClaimTrackingLog claimLog) {
		this.repository.save(claimLog);
	}

	@Override
	public void unbind(final ClaimTrackingLog claimLog) {
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Dataset dataset = super.unbindObject(claimLog, "creationMoment", "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "resolutionDescription", "published", "compensation", "status");
		if (claimLog.getClaim() != null)
			dataset.put("claimAcepted", claimLog.getIsAcepted());
		else
			dataset.put("claimAcepted", false);
		Collection<Claim> claims = this.claimRepository.findAllByAssistanceAgentId(agentId);
		SelectChoices claimChoices = SelectChoices.from(claims, "id", claimLog.getClaim());
		dataset.put("claim", claimChoices.getSelected().getKey());
		dataset.put("claims", claimChoices);
		SelectChoices stateChoices = SelectChoices.from(ClaimStatus.class, claimLog.getStatus());
		dataset.put("statuses", stateChoices);
		dataset.put("claim_readOnly", false);

		if (claimLog.isPublished()) {
			dataset.put("readonly", true);
			dataset.put("claim_readOnly", true);
		} else
			dataset.put("claim_readOnly", false);

		super.getResponse().addData(dataset);
	}

}
