
package acme.features.flight_crew.flight_assignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AssignmentStatus;
import acme.datatypes.Availability;
import acme.datatypes.CrewDuty;
import acme.entities.flight_assignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewUpdateFlightAssignmentService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	public FlightCrewUpdateFlightAssignmentService() {
	}

	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", Integer.TYPE);
		FlightAssignment assignment = this.repository.findById(id);
		FlightCrew loggedCrewMember = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
		int leg = assignment.getLeg().getId();
		CrewDuty loggedCrewMemberDuty = this.repository.findByAssigneeAndLeg(loggedCrewMember.getId(), leg).getDuty();
		Boolean status = loggedCrewMemberDuty.equals(CrewDuty.LEAD_ATTENDANT) && !assignment.getPublished();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", Integer.TYPE);
		FlightAssignment assignment = this.repository.findById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, new String[] {
			"duty", "last_update", "status", "remarks", "published"
		});
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		Boolean status = assignment.getAssignee().getAvailability() == Availability.AVAILABLE;
		if (status)
			super.state(status, "assignee", "assignee.is.not.available", new Object[0]);
		else {
			status = this.repository.findFlightAssignmentByAssigneeId(assignment.getAssignee().getId()).stream().anyMatch((a) -> {
				return a.existsConflict(assignment);
			});
			if (status)
				super.state(status, "leg", "conflicting.assignment.with.previous.assignment", new Object[0]);

			if (assignment.getDuty().equals(CrewDuty.PILOT))
				status = this.repository.findByLegId(assignment.getLeg().getId()).stream().anyMatch((a) -> {
					return a.getDuty().equals(CrewDuty.PILOT);
				});

			if (status)
				super.state(status, "duty", "already.a.pilot.on.duty", new Object[0]);
			else {
				if (assignment.getDuty().equals(CrewDuty.CO_PILOT))
					status = this.repository.findByLegId(assignment.getLeg().getId()).stream().anyMatch((a) -> {
						return a.getDuty().equals(CrewDuty.CO_PILOT);
					});

				if (status)
					super.state(status, "duty", "already.a.co-pilot.on.duty", new Object[0]);
			}
		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		SelectChoices dutyChoices = SelectChoices.from(CrewDuty.class, assignment.getDuty());
		SelectChoices statusChoices = SelectChoices.from(AssignmentStatus.class, assignment.getStatus());
		Dataset dataset = super.unbindObject(assignment, new String[] {
			"duty", "lastUpdate", "status", "remarks", "published"
		});
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("duty", dutyChoices);
		dataset.put("status", statusChoices);
		super.getResponse().addData(dataset);
	}
}
