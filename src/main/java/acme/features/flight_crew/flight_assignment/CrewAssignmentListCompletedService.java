
package acme.features.flight_crew.flight_assignment;

import java.time.Instant;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight_assignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiService
public class CrewAssignmentListCompletedService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int assignee = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<FlightAssignment> assignments = this.repository.findFlightAssignmentByAssigneeId(assignee);
		Boolean status = assignments.stream().allMatch((l) -> {
			return l.getAssignee().getId() == assignee;
		});
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int assignee = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<FlightAssignment> assignments = this.repository.findFlightAssignmentByAssigneeId(assignee).stream().filter((a) -> {
			return a.getLeg().getScheduledArrival().before(java.util.Date.from(Instant.now()));
		}).toList();
		super.getBuffer().addData(assignments);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset = super.unbindObject(assignment, new String[] {
			"duty", "last_update", "status", "remarks", "published"
		});
		super.getResponse().addData(dataset);
	}
}
