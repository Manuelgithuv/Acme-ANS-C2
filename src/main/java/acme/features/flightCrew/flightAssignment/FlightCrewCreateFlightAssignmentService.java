
package acme.features.flightCrew.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Availability;
import acme.datatypes.CrewDuty;
import acme.entities.flight_assignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewCreateFlightAssignmentService extends AbstractGuiService<FlightCrew, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		// comprobamos que quien crea el assignment es lead attendant
		FlightCrew loggedCrewMember = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
		int id = super.getRequest().getData("id", int.class);
		int leg = this.repository.findById(id).getLeg().getId();

		CrewDuty loggedCrewMemberDuty = this.repository.findByAssigneeAndLeg(loggedCrewMember.getId(), leg).getDuty();

		super.getResponse().setAuthorised(loggedCrewMemberDuty.equals(CrewDuty.LEAD_ATTENDANT));
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		assignment = new FlightAssignment();

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "last_update", "status", "remarks", "published");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		Boolean status;

		// comprobamos que a quien se le asigna el assignment esta disponible
		status = assignment.getAssignee().getAvailability() == Availability.AVAILABLE;
		if (status) {
			super.state(status, "assignee", "assignee.is.not.available");
			return;
		}

		// comprobamos que a quien se le asigna no tiene otros tramos asignados a la vez
		status = this.repository.findFlightAssignmentByAssigneeId(assignment.getAssignee().getId()).stream().anyMatch(a -> a.existsConflict(assignment));
		if (status)
			super.state(status, "leg", "conflicting.assignment.with.previous.assignment");

		// comprobamos que solo haya un piloto
		if (assignment.getDuty().equals(CrewDuty.PILOT))
			status = this.repository.findByLegId(assignment.getLeg().getId()).stream().anyMatch(a -> a.getDuty().equals(CrewDuty.PILOT));
		if (status) {
			super.state(status, "duty", "already.a.pilot.on.duty");
			return;
		}

		// comprobamos que solo haya un co-piloto
		if (assignment.getDuty().equals(CrewDuty.CO_PILOT))
			status = this.repository.findByLegId(assignment.getLeg().getId()).stream().anyMatch(a -> a.getDuty().equals(CrewDuty.CO_PILOT));
		if (status) {
			super.state(status, "duty", "already.a.co-pilot.on.duty");
			return;
		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		dataset = super.unbindObject(assignment, "duty", "last_update", "status", "remarks", "published");

		super.getResponse().addData(dataset);
	}

}
