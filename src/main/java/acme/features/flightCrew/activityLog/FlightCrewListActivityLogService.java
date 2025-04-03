
package acme.features.flightCrew.activityLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewListActivityLogService extends AbstractGuiService<FlightCrew, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int assignee = super.getRequest().getPrincipal().getActiveRealm().getId();
		List<ActivityLog> logs = this.repository.findByFlightCrewId(assignee);

		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		dataset = super.unbindObject(log, "registration_moment", "incident_type", "description", "severity", "flight_assignment", "leg");

		super.getResponse().addData(dataset);
	}

}
