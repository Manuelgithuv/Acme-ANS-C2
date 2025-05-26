
package acme.features.assistanceAgents.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claimLog.ClaimTrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, ClaimTrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;


	@Override
	public void authorise() {
		boolean status;

		AssistanceAgent agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		status = agent.getClass().equals(AssistanceAgent.class);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<ClaimTrackingLog> claimsLogs;
		int assistanceAgentId;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();

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
