
package acme.features.assistanceAgents.trackingLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claimLog.ClaimTrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, ClaimTrackingLog> {

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;
}
