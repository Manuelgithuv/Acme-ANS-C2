
package acme.features.flight_crew.flight_assignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.CrewDuty;
import acme.entities.flight_assignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiService
public class CrewAssignmentDeleteService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


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
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.delete(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset = super.unbindObject(assignment, new String[] {
			"duty", "last_update", "status", "remarks", "published"
		});
		super.getResponse().addData(dataset);
	}
}
