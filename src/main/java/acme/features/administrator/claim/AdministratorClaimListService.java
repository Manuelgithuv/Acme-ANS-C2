
package acme.features.administrator.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.features.assistanceAgents.claim.AssistanceAgentClaimRepository;

@GuiService
public class AdministratorClaimListService extends AbstractGuiService<Administrator, Claim> {

	@Autowired
	private AssistanceAgentClaimRepository repository;


	@Override
	public void authorise() {
		Boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Claim> publishedClaims = this.repository.findAllPublishedClaims();

		super.getBuffer().addData(publishedClaims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;

		dataset = super.unbindObject(claim, "registrationMoment", "published");
		dataset.put("status", claim.getStatus());
		dataset.put("legId", claim.getLeg().getId());
		dataset.put("claimNameId", claim.getId());

		super.addPayload(dataset, claim);

		super.getResponse().addData(dataset);
	}
}
