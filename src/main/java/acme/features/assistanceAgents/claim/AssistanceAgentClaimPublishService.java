
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
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

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

		boolean legCheck = true;

		if (!super.getRequest().getMethod().equals("GET")) {

			int legId = super.getRequest().hasData("legId") ? super.getRequest().getData("legId", int.class) : 0;
			Leg leg = this.legRepository.findById(legId);
			if (leg == null || this.legChoices().stream().anyMatch(z -> z.getId() == legId))
				legCheck = true;
			else
				legCheck = false;
		}

		status = claim != null && claim.getAssistanceAgent().getId() == agentId && !claim.isPublished() && legCheck;

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
		super.bindObject(claim, "passengerEmail", "description", "type");
		claim.setLeg(leg);
	}

	@Override
	public void validate(final Claim claim) {
		if (claim.getLeg() == null)
			super.state(false, "legId", "assistance-agent.claim.create.legId");
		if (claim.getRegistrationMoment() != null && claim.getLeg() != null && claim.getRegistrationMoment().before(claim.getLeg().getScheduledArrival()))
			super.state(false, "legId", "assistance-agent.claim.create.legId.beforeLeg");

		//TODO: Si un claim se publica pero tiene status pending, es una accion legal?
	}

	@Override
	public void perform(final Claim claim) {
		claim.setPublished(true);
		this.repository.save(claim);

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
