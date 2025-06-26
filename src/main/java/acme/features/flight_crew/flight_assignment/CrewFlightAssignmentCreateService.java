
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
		Boolean isAuthorised;

		FlightCrew user = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
		String airlineCode = user.getAirline().getIataCode();

		if (!super.getRequest().getMethod().equals("GET"))
			try {
				Leg selectedLeg = super.getRequest().getData("leg", Leg.class);
				Boolean validLeg = selectedLeg == null ? true
					: this.legRepository.findAllLegs().stream() //
						.filter(x -> x.getFlightCode().contains(airlineCode)) //
						.filter(x -> x.isPublished()) //
						.anyMatch(x -> x.getId() == selectedLeg.getId());

				FlightCrew selectedAssignee = super.getRequest().getData("assignee", FlightCrew.class);
				Boolean validAssignee = selectedAssignee == null ? true
					: this.crewRepository.findAllByAirline(user.getAirline().getId()).stream() //
						// .filter(x -> x.getAvailability().equals(Availability.AVAILABLE)) //
						.anyMatch(x -> x.getId() == selectedAssignee.getId());

				isAuthorised = validLeg && validAssignee;
			} catch (Exception e) {
				isAuthorised = false;
			}
		else
			isAuthorised = true;

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

		// comprobamos que sólo haya un lead-attendant
		if (assignment.getDuty().equals(CrewDuty.LEAD_ATTENDANT)) {
			status = this.repository.findByLegId(assignment.getLeg().getId()).stream() //
				.filter(a -> !a.equals(assignment)) //
				.anyMatch(a -> a.getDuty().equals(CrewDuty.LEAD_ATTENDANT));
			if (status) {
				super.state(!status, "duty", "flight-crew.flight-assignment.constraint.already-assigned-lead-attendant", new Object[0]);
				return;
			}
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

		// comprobamos que la persona esté disponible
		status = !assignment.getAssignee().getAvailability().equals(Availability.AVAILABLE);
		if (status) {
			super.state(!status, "assignee", "flight-crew.flight-assignment.constraint.assignee-not-available", new Object[0]);
			return;
		}

		// comprobamos que no haya conflicto con otras designaciones
		status = this.repository.findAllFlightAssignment().stream() //
			.filter(a -> a.getAssignee().equals(assignment.getAssignee())) //
			.filter(a -> !a.equals(assignment)) //
			.anyMatch(a -> a.existsConflict(assignment) || a.getLeg().equals(assignment.getLeg()));
		if (status) {
			super.state(!status, "*", "flight-crew.flight-assignment.constraint.conflicting-assignment", new Object[0]);
			return;
		}

		//
		status = this.repository.findByAssigneeAndLeg(assignment.getAssignee(), assignment.getLeg().getId()) != null //
			&& this.repository.findByAssigneeAndLeg(assignment.getAssignee(), assignment.getLeg().getId()).getId() != assignment.getId();
		if (status) {
			super.state(!status, "*", "flight-crew.flight-assignment.constraint.already-assignment-for-pair", new Object[0]);
			return;
		}

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
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
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

		super.getResponse().addData(dataset);
	}
}
