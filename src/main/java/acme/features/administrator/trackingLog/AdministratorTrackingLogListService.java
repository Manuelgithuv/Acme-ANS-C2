
package acme.features.administrator.trackingLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claimLog.ClaimTrackingLog;
import acme.features.assistanceAgents.claim.AssistanceAgentClaimRepository;
import acme.features.assistanceAgents.trackingLog.AssistanceAgentTrackingLogRepository;
import acme.features.manager.leg.LegRepository;

@GuiService
public class AdministratorTrackingLogListService extends AbstractGuiService<Administrator, ClaimTrackingLog> {

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

		id = super.getRequest().hasData("claimId") ? super.getRequest().getData("claimId", int.class) : 0;
		claim = this.claimRepository.findClaimById(id);
		boolean claimCheck = false;
		if (claim != null && claim.isPublished())
			claimCheck = true;
		Boolean status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class);
		super.getResponse().setAuthorised(status && claimCheck);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("claimId", int.class);
		Collection<ClaimTrackingLog> logs = this.logRepository.findAllByClaimIdOrderByCreationMomentDescIdDesc(id);
		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ClaimTrackingLog claimLog) {
		Dataset dataset;

		dataset = super.unbindObject(claimLog, "lastUpdateMoment", "resolutionPercentage", "published");
		dataset.put("claim", claimLog.getClaim().getId());

		super.addPayload(dataset, claimLog);

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<ClaimTrackingLog> claimTrackingLog) {

		int claimId = super.getRequest().getData("claimId", int.class);
		super.getResponse().addGlobal("claimId", claimId);
	}

}
