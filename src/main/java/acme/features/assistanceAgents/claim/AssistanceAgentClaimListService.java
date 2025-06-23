
package acme.features.assistanceAgents.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.entities.claim.Claim;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


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
		Collection<Claim> claims;
		int assistanceAgentId;

		Boolean ongoing = super.getRequest().hasData("ongoing") ? super.getRequest().getData("ongoing", Boolean.class) : false;

		assistanceAgentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		claims = this.repository.findAllByAssistanceAgentId(assistanceAgentId);

		if (ongoing)
			claims = claims.stream().filter(z -> z.getStatus().equals(ClaimStatus.PENDING)).toList();
		else
			claims = claims.stream().filter(z -> z.getStatus().equals(ClaimStatus.ACCEPTED) || z.getStatus().equals(ClaimStatus.REJECTED)).toList();

		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "published");
		dataset.put("status", claim.getStatus());
		dataset.put("legId", claim.getLeg().getId());

		super.addPayload(dataset, claim);

		super.getResponse().addData(dataset);
	}
}
