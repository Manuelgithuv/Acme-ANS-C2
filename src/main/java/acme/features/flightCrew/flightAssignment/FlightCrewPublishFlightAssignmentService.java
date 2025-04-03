
package acme.features.flightCrew.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.datatypes.CrewDuty;
import acme.entities.flight_assignment.FlightAssignment;
import acme.realms.FlightCrew;

public class FlightCrewPublishFlightAssignmentService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findById(id);

		FlightCrew loggedCrewMember = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
		int leg = assignment.getLeg().getId();
		CrewDuty loggedCrewMemberDuty = this.repository.findByAssigneeAndLeg(loggedCrewMember.getId(), leg).getDuty();

		Boolean status = loggedCrewMemberDuty.equals(CrewDuty.LEAD_ATTENDANT) 	// comprobamos que quien crea el assignment es lead attendant
			&& !assignment.getPublished(); 										// comprobamos que el assignment no este publicado

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		FlightAssignment assignment = this.repository.findById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "last_update", "status", "remarks", "published");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setPublished(true);
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		dataset = super.unbindObject(assignment, "duty", "last_update", "status", "remarks", "published");
		super.getResponse().addData(dataset);
	}

}
