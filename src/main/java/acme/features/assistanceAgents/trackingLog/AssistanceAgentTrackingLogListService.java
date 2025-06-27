
package acme.features.assistanceAgents.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claimLog.ClaimTrackingLog;
import acme.features.assistanceAgents.claim.AssistanceAgentClaimRepository;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, ClaimTrackingLog> {

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
		if (super.getRequest().hasData("claimId")) {
			int claimId = super.getRequest().hasData("claimId") ? super.getRequest().getData("claimId", int.class) : 0;
			Claim claim = this.claimRepository.findClaimById(claimId);
			claimCheck = claim != null && claim.getAssistanceAgent().getId() == agentId;
		}

		status = agent.getClass().equals(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<ClaimTrackingLog> claimsLogs;
		int assistanceAgentId;

		int claimId = super.getRequest().hasData("claimId") ? super.getRequest().getData("claimId", int.class) : 0;
		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (claimId != 0)
			claimsLogs = this.repository.findAllClaimTrackingLogByAssistanceAgentId(assistanceAgentId).stream().filter(z -> z.getClaim().getId() == claimId).toList();
		else
			claimsLogs = this.repository.findAllClaimTrackingLogByAssistanceAgentId(assistanceAgentId);

		super.getBuffer().addData(claimsLogs);
	}

	@Override
	public void unbind(final ClaimTrackingLog claimLog) {
		Dataset dataset;

		dataset = super.unbindObject(claimLog, "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "published");
		dataset.put("claim", claimLog.getClaim().getId());

		super.addPayload(dataset, claimLog);

		super.getResponse().addData(dataset);
	}
}
