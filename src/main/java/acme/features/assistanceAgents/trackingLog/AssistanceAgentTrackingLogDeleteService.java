
package acme.features.assistanceAgents.trackingLog;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.entities.claim.Claim;
import acme.entities.claimLog.ClaimTrackingLog;
import acme.features.assistanceAgents.claim.AssistanceAgentClaimRepository;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogDeleteService extends AbstractGuiService<AssistanceAgent, ClaimTrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository	repository;

	@Autowired
	private AssistanceAgentClaimRepository			claimRepository;


	@Override
	public void authorise() {

		boolean status;

		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		int id = super.getRequest().hasData("id") ? super.getRequest().getData("id", int.class) : 0;

		ClaimTrackingLog claimLog = this.repository.findClaimTrackingLogById(id);

		status = claimLog != null && claimLog.getClaim().getAssistanceAgent().getId() == agentId && !claimLog.isPublished();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		ClaimTrackingLog claimLog = this.repository.findClaimTrackingLogById(id);
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

	}

	@Override
	public void perform(final ClaimTrackingLog claimLog) {
		this.repository.delete(claimLog);
	}

	@Override
	public void unbind(final ClaimTrackingLog claimLog) {
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Dataset dataset = super.unbindObject(claimLog, "creationMoment", "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "resolutionDescription", "published", "compensation", "status");
		dataset.put("claimAcepted", claimLog.getIsAcepted());
		Collection<Claim> claims = List.of(claimLog.getClaim());
		SelectChoices claimChoices = SelectChoices.from(claims, "id", claimLog.getClaim());
		dataset.put("claim", claimChoices.getSelected().getKey());
		dataset.put("claims", claimChoices);
		SelectChoices stateChoices = SelectChoices.from(ClaimStatus.class, claimLog.getStatus());
		dataset.put("statuses", stateChoices);
		dataset.put("claim_readOnly", true);
		super.getResponse().addData(dataset);
	}
}
