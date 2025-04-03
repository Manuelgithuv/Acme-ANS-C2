
package acme.features.flightCrew.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewCreateActivityLogService extends AbstractGuiService<FlightCrew, ActivityLog> {

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
		ActivityLog log = new ActivityLog();
		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "registration_moment", "incident_type", "description", "severity", "flight_assignment", "leg");
	}

	@Override
	public void validate(final ActivityLog assignment) {
		;
	}

	@Override
	public void perform(final ActivityLog assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		dataset = super.unbindObject(log, "registration_moment", "incident_type", "description", "severity", "flight_assignment", "leg");

		super.getResponse().addData(dataset);
	}

}
