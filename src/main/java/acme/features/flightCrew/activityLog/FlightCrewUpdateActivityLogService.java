
package acme.features.flightCrew.activityLog;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewUpdateActivityLogService extends AbstractGuiService<FlightCrew, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		ActivityLog log = this.repository.findById(id);

		FlightCrew loggedCrewMember = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();

		Boolean status = log.getFlightAssignment().getAssignee().getId() == loggedCrewMember.getId() // comprobamos que el que creo el log es quien esta intentando eliminarlo
			&& !log.getPublished(); // y que no esté publicado

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog log;
		int id = super.getRequest().getData("id", int.class);

		log = this.repository.findById(id);
		super.getBuffer().addData(log);

	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "registration_moment", "incident_type", "description", "severity", "flight_assignment", "leg");
	}

	@Override
	public void validate(final ActivityLog log) {
		;
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		dataset = super.unbindObject(log, "registration_moment", "incident_type", "description", "severity", "flight_assignment", "leg");

		super.getResponse().addData(dataset);
	}

}
