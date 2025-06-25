
package acme.features.administrator.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimType;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.features.assistanceAgents.claim.AssistanceAgentClaimRepository;
import acme.features.assistanceAgents.trackingLog.AssistanceAgentTrackingLogRepository;
import acme.features.manager.leg.LegRepository;

@GuiService
public class AdministratorClaimShowService extends AbstractGuiService<Administrator, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository			claimRepository;

	@Autowired
	private AssistanceAgentTrackingLogRepository	logRepository;

	@Autowired
	private LegRepository							legRepository;


	@Override
	public void authorise() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.claimRepository.findClaimById(id);

		if (!claim.isPublished())
			super.getResponse().setAuthorised(false);
		Boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.claimRepository.findClaimById(id);
		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset = super.unbindObject(claim, "registrationMoment", "passengerEmail", "description", "type", "published");
		dataset.put("status", claim.getStatus());
		Collection<Leg> legs = this.legRepository.findAllLegs();
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
