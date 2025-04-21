
package acme.features.flight_crew.activity_log;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.entities.flight_assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.features.flight_crew.flight_assignment.FlightAssignmentRepository;
import acme.features.manager.leg.LegRepository;
import acme.realms.FlightCrew;

@GuiService
public class CrewActivityLogPublishService extends AbstractGuiService<FlightCrew, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ActivityLogRepository		repository;
	@Autowired
	private LegRepository				legRepository;
	@Autowired
	private FlightAssignmentRepository	assignmentRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		int id = super.getRequest().getData("id", Integer.TYPE);
		ActivityLog log = this.repository.findById(id);
		FlightCrew loggedCrewMember = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
		Boolean status = log.getFlightAssignment().getAssignee().getId() == loggedCrewMember.getId() && !log.getPublished();
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", Integer.TYPE);
		ActivityLog log = this.repository.findById(id);
		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, new String[] {
			"registrationMoment", "incidentType", "description", "severity"
		});
		String legFlightCode = super.getRequest().getData("leg.flightCode", String.class);
		Optional<Leg> leg = this.legRepository.findByFlightCode(legFlightCode);
		System.out.println("\n\n#### leg: " + legFlightCode + "\n\n");
		if (leg.isPresent())
			log.setLeg(leg.get());
		else
			super.state(false, "leg.flightCode", "flight-crew.activity-log.invalidFlightCode", new Object[0]);

		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		FlightAssignment assignment = this.assignmentRepository.findByAssigneeAndLeg(userAccountId, leg.get().getId());
		log.setFlightAssignment(assignment);
	}

	@Override
	public void validate(final ActivityLog log) {
	}

	@Override
	public void perform(final ActivityLog log) {
		log.setPublished(true);
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		SelectChoices legChoices = SelectChoices.from(this.legRepository.findAllLegs(), "flightCode", log.getLeg());
		Dataset dataset = super.unbindObject(log, new String[] {
			"registrationMoment", "incidentType", "description", "severity"
		});
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("legs", legChoices);
		dataset.put("leg.flightCode", legChoices.getSelected().getKey());
		super.getResponse().addData(dataset);
	}
}
