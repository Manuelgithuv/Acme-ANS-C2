
package acme.features.flight_crew.flight_assignment;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.FlightCrewRepository;
import acme.datatypes.AssignmentStatus;
import acme.datatypes.CrewDuty;
import acme.entities.activity_log.ActivityLog;
import acme.entities.flight_assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.features.flight_crew.activity_log.ActivityLogRepository;
import acme.features.manager.leg.LegRepository;
import acme.realms.FlightCrew;

@GuiService
public class CrewFlightAssignmentDeleteService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightAssignmentRepository	repository;
	@Autowired
	private ActivityLogRepository		logRepository;
	@Autowired
	private LegRepository				legRepository;
	@Autowired
	private FlightCrewRepository		crewRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isAuthorised;
		int id;

		try {
			id = super.getRequest().hasData("id") ? super.getRequest().getData("id", int.class) : 0;

			FlightAssignment assignment = this.repository.findById(id);
			isAuthorised = assignment != null;
			if (!isAuthorised) {
				super.getResponse().setAuthorised(isAuthorised);
				return;
			}

			FlightCrew user = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
			FlightCrew leadAttendant = this.repository.findByLegId(assignment.getLeg().getId()).stream() //
				.filter(a -> a.getDuty().equals(CrewDuty.LEAD_ATTENDANT)) //
				.map(a -> a.getAssignee()) //
				.toList().get(0);

			isAuthorised = leadAttendant.equals(user) //
				&& !assignment.getPublished();
		} catch (Exception e) {
			isAuthorised = false;
		}

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
		Collection<FlightAssignment> assignments = this.repository.findAllFlightAssignment().stream() //
			.filter(x -> x.getLeg().equals(assignment.getLeg())) //
			.filter(x -> !x.getDuty().equals(CrewDuty.LEAD_ATTENDANT)).toList();

		if (assignments.size() > 0 && assignment.getDuty().equals(CrewDuty.LEAD_ATTENDANT)) {
			super.state(false, "*", "flight-crew.flight-assignment.constraint.cannot-delete-assignment", new Object[0]);
			return;
		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		Collection<ActivityLog> logs = this.logRepository.findByFlightAssignmentId(assignment.getId());
		logs.stream().forEach(log -> this.logRepository.delete(log));

		this.repository.delete(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		FlightCrew crew = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();

		Collection<FlightAssignment> allAssignments = this.repository.findAllFlightAssignment();
		List<Leg> legsAsLeadAttendant = allAssignments.stream() //
			.filter(a -> a.getAssignee().equals(crew)) //
			.filter(a -> a.getDuty().equals(CrewDuty.LEAD_ATTENDANT)) //
			.map(a -> a.getLeg()) //
			.toList();

		Dataset dataset;
		dataset = super.unbindObject(assignment, "lastUpdate", "remarks", "published");

		SelectChoices dutyChoices = SelectChoices.from(CrewDuty.class, assignment.getDuty());
		dataset.put("duty", dutyChoices.getSelected().getKey());
		dataset.put("duties", dutyChoices);

		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		dataset.put("status", statusChoices.getSelected().getKey());
		dataset.put("statuses", statusChoices);

		String airlineCode = crew.getAirline().getIataCode();
		List<Leg> legs = this.legRepository.findAllLegs().stream() // traemos todos los tramos de vuelo disponible
			.filter(x -> x.getFlightCode().contains(airlineCode)) // filtramos por aerolinea
			.filter(x -> x.isPublished()) // filtramos por publicados
			.toList();
		SelectChoices legChoices = SelectChoices.from(legs, "flightCode", assignment.getLeg());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		Collection<FlightCrew> assignees = this.crewRepository.findAllByAirline(crew.getAirline().getId()).stream() //
			//.filter(x -> x.getAvailability().equals(Availability.AVAILABLE)) //
			.toList();
		SelectChoices assigneeChoices = SelectChoices.from(assignees, "identifier", assignment.getAssignee());
		dataset.put("assignee", assigneeChoices.getSelected().getKey());
		dataset.put("assignees", assigneeChoices);

		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		Boolean authorised = legsAsLeadAttendant.contains(assignment.getLeg()) && !assignment.getPublished();
		dataset.put("authorised", authorised);
		Boolean canPublish = authorised && assignment.getLeg().isPublished();
		dataset.put("canPublish", canPublish);

		Boolean duty_readOnly = assignment.getDuty().equals(CrewDuty.LEAD_ATTENDANT);
		dataset.put("duty_readOnly", duty_readOnly);

		super.getResponse().addData(dataset);
	}
}
