
package acme.features.flight_crew.flight_assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.CrewDuty;
import acme.entities.activity_log.ActivityLog;
import acme.entities.flight_assignment.FlightAssignment;
import acme.features.flight_crew.activity_log.ActivityLogRepository;
import acme.realms.FlightCrew;

@GuiService
public class CrewFlightAssignmentDeleteService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightAssignmentRepository	repository;
	@Autowired
	private ActivityLogRepository		logRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isAuthorised;

		int id = super.getRequest().getData("id", Integer.TYPE);
		FlightAssignment assignment = this.repository.findById(id);
		FlightCrew user = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
		FlightCrew leadAttendant = this.repository.findByLegId(assignment.getLeg().getId()).stream() //
			.filter(a -> a.getDuty().equals(CrewDuty.LEAD_ATTENDANT)) //
			.map(a -> a.getAssignee()) //
			.toList().get(0);

		isAuthorised = leadAttendant.equals(user) //
			&& !assignment.getPublished();

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", Integer.TYPE);
		FlightAssignment assignment = this.repository.findById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment);
	}

	@Override
	public void validate(final FlightAssignment assignment) {
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		Collection<ActivityLog> logs = this.logRepository.findByFlightAssignmentId(assignment.getId());
		logs.stream().forEach(log -> this.logRepository.delete(log));

		this.repository.delete(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "remarks", "published");
		super.getResponse().addData(dataset);
	}
}
