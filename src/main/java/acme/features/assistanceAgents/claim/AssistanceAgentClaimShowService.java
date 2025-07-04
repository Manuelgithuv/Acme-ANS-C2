
package acme.features.assistanceAgents.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimType;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.features.manager.leg.LegRepository;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository	repository;

	@Autowired
	private LegRepository					legRepository;


	@Override
	public void authorise() {

		boolean status;

		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		int id = super.getRequest().hasData("id") ? super.getRequest().getData("id", int.class) : 0;

		Claim claim = this.repository.findClaimById(id);

		status = claim != null && claim.getAssistanceAgent().getId() == agentId;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Claim claim = this.repository.findClaimById(id);
		super.getBuffer().addData(claim);
	}

	public Collection<Leg> legChoices() {
		return this.legRepository.findAllLegs().stream().filter(z -> z.isPublished()).toList();
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "published");
		dataset.put("status", claim.getStatus());
		Collection<Leg> legs = this.legChoices();
		SelectChoices legChoices = SelectChoices.from(legs, "id", claim.getLeg());
		dataset.put("legId", legChoices.getSelected().getKey());
		dataset.put("legIds", legChoices);
		SelectChoices typeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		dataset.put("types", typeChoices);
		if (claim.isPublished())
			dataset.put("readonly", true);

		super.getResponse().addData(dataset);
	}
}
