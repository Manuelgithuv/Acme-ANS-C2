
package acme.features.flight_crew.activity_log;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
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
		Boolean status;
		int id;

		try {
			id = super.getRequest().hasData("id") ? super.getRequest().getData("id", int.class) : 0;

			ActivityLog log = this.repository.findById(id);
			status = log != null;
			if (!status) {
				super.getResponse().setAuthorised(status);
				return;
			}

			FlightCrew user = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
			status = log.getFlightAssignment().getAssignee().getId() == user.getId() //
				&& !log.getPublished() //
				&& log.getFlightAssignment().getPublished();
		} catch (Exception e) {
			status = false;
		}

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
		super.bindObject(log);
	}

	@Override
	public void validate(final ActivityLog log) {
		if (!log.getFlightAssignment().getPublished())
			super.state(false, "*", "flight-crew.activity-log.constraint.assignment-not-published", new Object[0]);
	}

	@Override
	public void perform(final ActivityLog log) {
		log.setPublished(true);
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		SelectChoices legChoices = SelectChoices.from(this.legRepository.findAllLegs(), "flightCode", log.getLeg());
		Dataset dataset = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severity");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("legs", legChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		super.getResponse().addData(dataset);
	}
}
