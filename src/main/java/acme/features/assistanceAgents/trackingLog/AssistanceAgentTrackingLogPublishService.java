
package acme.features.assistanceAgents.trackingLog;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.entities.claim.Claim;
import acme.entities.claimLog.ClaimTrackingLog;
import acme.features.assistanceAgents.claim.AssistanceAgentClaimRepository;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogPublishService extends AbstractGuiService<AssistanceAgent, ClaimTrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository	repository;

	@Autowired
	private AssistanceAgentClaimRepository			claimRepository;


	@Override
	public void authorise() {

		boolean status;

		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		int id = super.getRequest().hasData("id") ? super.getRequest().getData("id", int.class) : 0;

		ClaimTrackingLog claimLog = this.repository.findClaimTrackingLogById(id);
		boolean claimCheck = true;
		/*
		 * if (!super.getRequest().getMethod().equals("GET")) {
		 * 
		 * int claimId = super.getRequest().hasData("claim") ? super.getRequest().getData("claim", int.class) : 0;
		 * Claim claim = this.claimRepository.findClaimById(claimId);
		 * if (claimId == 0)
		 * claimCheck = true;
		 * else if (claim != null && claim.getAssistanceAgent().getId() == agentId)
		 * claimCheck = true;
		 * else
		 * claimCheck = false;
		 * }
		 */
		status = claimLog != null && claimLog.getClaim().getAssistanceAgent().getId() == agentId && !claimLog.isPublished() && claimCheck;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		ClaimTrackingLog claimLog = this.repository.findClaimTrackingLogById(id);
		super.getBuffer().addData(claimLog);
	}

	@Override
	public void bind(final ClaimTrackingLog claimLog) {

		String money = super.getRequest().getData("compensation", String.class);
		if (money == "") {
			claimLog.setCompensation(null);
			super.bindObject(claimLog, "stepUndergoing", "resolutionPercentage", "resolutionDescription", "status");
		} else
			super.bindObject(claimLog, "stepUndergoing", "resolutionPercentage", "resolutionDescription", "compensation", "status");

	}

	@Override
	public void validate(final ClaimTrackingLog claimLog) {

		if (claimLog.getClaim() == null)
			super.state(false, "claim", "assistance-agent.claim-tracking-log.create.claim");
		if (claimLog.getClaim() != null && !claimLog.getClaim().isPublished())
			super.state(false, "claim", "assistance-agent.claim-tracking-log.publish.claimNotPublished");
		if (claimLog != null && claimLog.getClaim() != null && claimLog.getResolutionPercentage() != null) {
			List<ClaimTrackingLog> logsOrdenados = List.copyOf(this.repository.findAllByClaimIdOrderByCreationMomentDescIdDesc(claimLog.getClaim().getId()));
			if (!logsOrdenados.isEmpty()) {
				int index = logsOrdenados.indexOf(claimLog);
				if (index != -1) {
					if (index < logsOrdenados.size() - 1) {
						ClaimTrackingLog next = logsOrdenados.get(index + 1);
						if (claimLog.getResolutionPercentage() < next.getResolutionPercentage())
							super.state(false, "resolutionPercentage", "assistance-agent.claim-tracking-log.update.ResolutionPercentage.greater-or-equal");
					}
					if (index > 0) {
						ClaimTrackingLog previous = logsOrdenados.get(index - 1);
						if (claimLog.getResolutionPercentage() > previous.getResolutionPercentage())
							super.state(false, "resolutionPercentage", "assistance-agent.claim-tracking-log.update.ResolutionPercentage.smaller-or-equal");
					}
				}
			}
		}

	}

	@Override
	public void perform(final ClaimTrackingLog claimLog) {
		claimLog.setPublished(true);
		claimLog.setLastUpdateMoment(MomentHelper.getCurrentMoment());
		this.repository.save(claimLog);
	}

	@Override
	public void unbind(final ClaimTrackingLog claimLog) {
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Dataset dataset = super.unbindObject(claimLog, "creationMoment", "lastUpdateMoment", "stepUndergoing", "resolutionPercentage", "resolutionDescription", "published", "compensation", "status");
		if (claimLog.getClaim() != null)
			dataset.put("claimAcepted", claimLog.getIsAcepted());
		else
			dataset.put("claimAcepted", false);
		Collection<Claim> claims = List.of(claimLog.getClaim());
		SelectChoices claimChoices = SelectChoices.from(claims, "id", claimLog.getClaim());
		dataset.put("claim", claimChoices.getSelected().getKey());
		dataset.put("claims", claimChoices);

		SelectChoices stateChoices = SelectChoices.from(ClaimStatus.class, claimLog.getStatus());
		dataset.put("statuses", stateChoices);
		dataset.put("claim_readOnly", true);
		if (claimLog.isPublished())
			dataset.put("readonly", true);
		super.getResponse().addData(dataset);
	}
}
