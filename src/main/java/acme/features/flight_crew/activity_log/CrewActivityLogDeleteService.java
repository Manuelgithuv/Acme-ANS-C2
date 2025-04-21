
package acme.features.flight_crew.activity_log;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.realms.FlightCrew;

@GuiService
public class CrewActivityLogDeleteService extends AbstractGuiService<FlightCrew, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ActivityLogRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", Integer.TYPE);
		ActivityLog log = this.repository.findById(id);
		FlightCrew loggedCrewMember = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
		Boolean status = log.getFlightAssignment().getAssignee().getId() == loggedCrewMember.getId() && !log.getPublished();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", Integer.TYPE);
		ActivityLog log = this.repository.findById(id);
		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, new String[] {
			"registration_moment", "incident_type", "description", "severity", "flight_assignment", "leg"
		});
	}

	@Override
	public void validate(final ActivityLog log) {
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.delete(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset = super.unbindObject(log, new String[] {
			"registration_moment", "incident_type", "description", "severity", "flight_assignment", "leg"
		});
		super.getResponse().addData(dataset);
	}
}
