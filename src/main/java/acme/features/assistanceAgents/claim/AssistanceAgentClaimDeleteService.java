
package acme.features.assistanceAgents.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.datatypes.ClaimType;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.features.manager.leg.LegRepository;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

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

		status = claim.getAssistanceAgent().getId() == agentId && !claim.isPublished();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Claim claim = this.repository.findClaimById(id);
		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId = super.getRequest().getData("legId", int.class);
		Leg leg = this.legRepository.findById(legId);
		super.bindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "status");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {

		if (claim.getLastTrackingLog() != null)
			super.state(false, "*", "assistance-agent.claim.delete.pendingLogs");

	}

	@Override
	public void perform(final Claim claim) {
		this.repository.delete(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "status", "published");
		Collection<Leg> legs = this.legRepository.findAllLegs();
		SelectChoices legChoices = SelectChoices.from(legs, "id", claim.getLeg());
		dataset.put("legId", legChoices.getSelected().getKey());
		dataset.put("legIds", legChoices);
		SelectChoices typeChoices = SelectChoices.from(ClaimType.class, claim.getType());
		SelectChoices stateChoices = SelectChoices.from(ClaimStatus.class, claim.getStatus());
		dataset.put("types", typeChoices);
		dataset.put("statuses", stateChoices);
		if (claim.isPublished())
			dataset.put("readonly", true);

		super.getResponse().addData(dataset);
	}

}
