
package acme.features.flightCrew.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.entities.flight_assignment.FlightAssignment;
import acme.realms.FlightCrew;

public class FlightCrewShowFlightAssignmentsService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findById(id);

		super.getBuffer().addData(assignment);

	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		dataset = super.unbindObject(assignment, "duty", "last_update", "status", "remarks", "published");
		super.getResponse().addData(dataset);
	}

}
