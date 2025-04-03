
package acme.features.flight_crew.flight_assignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight_assignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewShowFlightAssignmentService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	public FlightCrewShowFlightAssignmentService() {
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", Integer.TYPE);
		FlightAssignment assignment = this.repository.findById(id);
		super.getBuffer().addData(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset = super.unbindObject(assignment, new String[] {
			"duty", "last_update", "status", "remarks", "published"
		});
		super.getResponse().addData(dataset);
	}
}
