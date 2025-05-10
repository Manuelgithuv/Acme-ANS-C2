
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
import acme.datatypes.Availability;
import acme.datatypes.CrewDuty;
import acme.entities.flight_assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.features.manager.leg.LegRepository;
import acme.realms.FlightCrew;

@GuiService
public class CrewFlightAssignmentUpdateService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightAssignmentRepository	repository;
	@Autowired
	private LegRepository				legRepository;
	@Autowired
	private FlightCrewRepository		crewRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isAuthorised;

		int id = super.getRequest().getData("id", Integer.TYPE);
		FlightCrew user = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
		FlightAssignment assignment = this.repository.findById(id);

		Collection<FlightAssignment> allAssignments = this.repository.findAllFlightAssignment();
		List<Leg> legsAsLeadAttendant = allAssignments.stream() //
			.filter(a -> a.getAssignee().equals(user)) //
			.filter(a -> a.getDuty().equals(CrewDuty.LEAD_ATTENDANT)) //
			.map(a -> a.getLeg()) //
			.toList();

		isAuthorised = legsAsLeadAttendant.contains(assignment.getLeg());

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
		super.bindObject(assignment, "lastUpdate", "remarks", "duty", "status", "assignee", "leg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		Boolean status;

		// comprobamos que ningun atributo sea nulo
		status = assignment.getDuty() == null || assignment.getLastUpdate() == null || assignment.getStatus() == null || assignment.getAssignee() == null || assignment.getLeg() == null;
		if (status) {
			super.state(!status, "*", "flight-crew.flight-assignment.constraint.null-value", new Object[0]);
			return;
		}

		// comprobamos que la persona esté disponible
		status = !assignment.getAssignee().getAvailability().equals(Availability.AVAILABLE);
		if (status) {
			super.state(!status, "assignee", "flight-crew.flight-assignment.constraint.assignee-not-available", new Object[0]);
			return;
		}

		// comprobamos que no haya conflicto con otras designaciones
		status = this.repository.findFlightAssignmentByAssigneeId(assignment.getAssignee().getId()).stream() //
			.filter(a -> !a.equals(assignment)) //
			.anyMatch(a -> a.existsConflict(assignment));

		if (status) {
			super.state(!status, "*", "flight-crew.flight-assignment.constraint.conflicting-assignment", new Object[0]);
			return;
		}

		// comprobamos que sólo haya un piloto
		if (assignment.getDuty().equals(CrewDuty.PILOT)) {
			status = this.repository.findByLegId(assignment.getLeg().getId()).stream() //
				.filter(a -> !a.equals(assignment)) //
				.anyMatch(a -> a.getDuty().equals(CrewDuty.PILOT));
			if (status) {
				super.state(!status, "duty", "flight-crew.flight-assignment.constraint.already-assigned-pilot", new Object[0]);
				return;
			}
		}
		// comprobamos que sólo haya un co-piloto
		if (assignment.getDuty().equals(CrewDuty.CO_PILOT)) {
			status = this.repository.findByLegId(assignment.getLeg().getId()).stream() //
				.filter(a -> !a.equals(assignment)) //
				.anyMatch(a -> a.getDuty().equals(CrewDuty.CO_PILOT));
			if (status) {
				super.state(!status, "duty", "flight-crew.flight-assignment.constraint.already-assigned-co-pilot", new Object[0]);
				return;
			}
		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		FlightCrew crew = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();

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
			.toList();
		SelectChoices legChoices = SelectChoices.from(legs, "flightCode", assignment.getLeg());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		Collection<FlightCrew> assignees = this.crewRepository.findAllByAirline(crew.getAirline().getId());
		SelectChoices assigneeChoices = SelectChoices.from(assignees, "identifier", assignment.getAssignee());
		dataset.put("assignee", assigneeChoices.getSelected().getKey());
		dataset.put("assignees", assigneeChoices);

		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
