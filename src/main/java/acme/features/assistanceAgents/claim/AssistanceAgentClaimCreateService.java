
package acme.features.assistanceAgents.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimType;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.features.manager.leg.LegRepository;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository	repository;

	@Autowired
	private LegRepository					legRepository;


	@Override
	public void authorise() {

		boolean status;

		Boolean realmCheck = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);

		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		boolean legCheck = true;

		if (realmCheck && !super.getRequest().getMethod().equals("GET")) {
			int legId = super.getRequest().hasData("legId") ? super.getRequest().getData("legId", int.class) : 0;
			if (legId == 0 || this.legChoices().stream().anyMatch(z -> z.getId() == legId))
				legCheck = true;
			else
				legCheck = false;
		}

		status = realmCheck && legCheck;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent agent;
		claim = new Claim();
		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		claim.setPublished(false);
		claim.setAssistanceAgent(agent);
		claim.setRegistrationMoment(MomentHelper.getCurrentMoment());

		if (!super.getRequest().getMethod().equals("GET")) {
			int legId = super.getRequest().getData("legId", int.class);
			Leg leg = this.legRepository.findById(legId);
			claim.setLeg(leg);
		}
		super.getBuffer().addData(claim);

	}

	@Override
	public void bind(final Claim claim) {

		super.bindObject(claim, "passengerEmail", "description", "type");
	}

	@Override
	public void validate(final Claim claim) {

		if (claim.getLeg() == null)
			super.state(false, "legId", "assistance-agent.claim.create.legId");
		if (claim.getRegistrationMoment() != null && claim.getLeg() != null && claim.getRegistrationMoment().before(claim.getLeg().getScheduledArrival()))
			super.state(false, "legId", "assistance-agent.claim.create.legId.beforeLeg");

	}

	@Override
	public void perform(final Claim claim) {
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
