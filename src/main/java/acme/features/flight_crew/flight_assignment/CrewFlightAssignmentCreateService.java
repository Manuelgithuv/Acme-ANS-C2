
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
public class CrewFlightAssignmentCreateService extends AbstractGuiService<FlightCrew, FlightAssignment> {

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
		boolean isAuthorised = true;
		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		FlightAssignment assignment = new FlightAssignment();
		assignment.setPublished(false);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "lastUpdate", "remarks", "duty", "status", "assignee", "leg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		Boolean status;
		FlightCrew user = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();

		// comprobamos que ningún atributo sea nulo
		status = assignment.getDuty() == null || assignment.getLastUpdate() == null || assignment.getStatus() == null || assignment.getAssignee() == null || assignment.getLeg() == null;
		if (status) {
			super.state(!status, "*", "flight-crew.flight-assignment.constraint.null-value", new Object[0]);
			return;
		}

		// comprobamos que no se haya cambiado el valor de los desplegables
		//status;
		System.out.println(assignment);
		System.out.println(assignment.getDuty());

		// comprobamos que el usuario sea lead attendant
		status = !assignment.getDuty().equals(CrewDuty.LEAD_ATTENDANT);
		if (status) {
			super.state(!status, "*", "flight-crew.flight-assignment.constraint.not-authorised", new Object[0]);
			return;
		}

		// o que sea el primer assignment para esa leg y que sea lead attendant
		Collection<FlightAssignment> assignmentsForLeg = this.repository.findByLegId(assignment.getLeg().getId());
		status = assignmentsForLeg.isEmpty() //
			&& !assignment.getAssignee().equals(user);
		if (status) {
			super.state(!status, "*", "flight-crew.flight-assignment.constraint.not-authorised", new Object[0]);
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
			.filter(x -> x.isPublished()) // filtramos por publicados
			.toList();
		SelectChoices legChoices = SelectChoices.from(legs, "flightCode", assignment.getLeg());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		Collection<FlightCrew> assignees = this.crewRepository.findAllByAirline(crew.getAirline().getId()).stream() //
			.filter(x -> x.getAvailability().equals(Availability.AVAILABLE)) //
			.toList();
		SelectChoices assigneeChoices = SelectChoices.from(assignees, "identifier", assignment.getAssignee());
		dataset.put("assignee", assigneeChoices.getSelected().getKey());
		dataset.put("assignees", assigneeChoices);

		dataset.put("confirmation", true);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
