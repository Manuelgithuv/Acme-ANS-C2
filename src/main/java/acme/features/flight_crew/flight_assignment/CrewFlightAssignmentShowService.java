
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
import acme.entities.flight_assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.features.manager.leg.LegRepository;
import acme.realms.FlightCrew;

@GuiService
public class CrewFlightAssignmentShowService extends AbstractGuiService<FlightCrew, FlightAssignment> {

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
				|| assignment.getAssignee().equals(user);
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
	public void unbind(final FlightAssignment assignment) {
		FlightCrew user = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();

		Collection<FlightAssignment> allAssignments = this.repository.findAllFlightAssignment();
		List<Leg> legsAsLeadAttendant = allAssignments.stream() //
			.filter(a -> a.getAssignee().equals(user)) //
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

		String airlineCode = user.getAirline().getIataCode();
		Collection<Leg> legs = this.legRepository.findAllLegs().stream() // traemos todos los tramos de vuelo disponible
			.filter(x -> x.getFlightCode().contains(airlineCode) && x.isPublished()) // filtramos por aerolinea y publico
			.toList();
		SelectChoices legChoices = SelectChoices.from(legs, "flightCode", assignment.getLeg());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		Collection<FlightCrew> assignees = this.crewRepository.findAllByAirline(user.getAirline().getId()).stream() //
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
