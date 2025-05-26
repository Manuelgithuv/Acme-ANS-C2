
package acme.features.flight_crew.flight_assignment;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.FlightCrewRepository;
import acme.datatypes.CrewDuty;
import acme.entities.flight_assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.features.manager.leg.LegRepository;
import acme.realms.FlightCrew;

@GuiService
public class CrewFlightAssignmentListPlannedService extends AbstractGuiService<FlightCrew, FlightAssignment> {

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
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightCrew user = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
		List<FlightAssignment> assignments;
		Date now = Date.from(Instant.now());

		Collection<FlightAssignment> allAssignments = this.repository.findAllFlightAssignment();
		List<Leg> legsAsLeadAttendant = allAssignments.stream() //
			.filter(a -> a.getAssignee().equals(user)) //
			.filter(a -> a.getDuty().equals(CrewDuty.LEAD_ATTENDANT)) //
			.map(a -> a.getLeg()) //
			.toList();
		assignments = allAssignments.stream() //
			.filter(a -> (a.getAssignee().equals(user) || legsAsLeadAttendant.contains(a.getLeg()))) //
			.filter(a -> a.getLeg().getScheduledDeparture().after(now)) // list planned
			.toList();

		super.getBuffer().addData(assignments);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		dataset = super.unbindObject(assignment, "duty", "lastUpdate", "status", "published");
		dataset.put("leg", assignment.getLeg().getFlightCode());
		dataset.put("assignee", assignment.getAssignee().getIdentifier());
		dataset.put("departure", assignment.getLeg().getScheduledDeparture());
		dataset.put("arrival", assignment.getLeg().getScheduledArrival());

		super.addPayload(dataset, assignment, "remarks");
		super.getResponse().addData(dataset);
	}
}
