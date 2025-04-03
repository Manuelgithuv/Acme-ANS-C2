
package acme.features.flightCrew.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.entities.activityLog.ActivityLog;
import acme.realms.FlightCrew;

public class FlightCrewShowActivityLogService extends AbstractGuiService<FlightCrew, ActivityLog> {

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
		ActivityLog log;
		int id = super.getRequest().getData("id", int.class);

		log = this.repository.findById(id);
		super.getBuffer().addData(log);

	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		dataset = super.unbindObject(log, "registration_moment", "incident_type", "description", "severity", "flight_assignment", "leg");

		super.getResponse().addData(dataset);
	}

}
