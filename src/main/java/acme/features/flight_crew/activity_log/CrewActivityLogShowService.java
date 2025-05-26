
package acme.features.flight_crew.activity_log;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.entities.leg.Leg;
import acme.features.flight_crew.flight_assignment.FlightAssignmentRepository;
import acme.realms.FlightCrew;

@GuiService
public class CrewActivityLogShowService extends AbstractGuiService<FlightCrew, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ActivityLogRepository		repository;
	@Autowired
	private FlightAssignmentRepository	assignmentRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		Boolean isAuthorised;
		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		int id;

		try {
			id = super.getRequest().hasData("id") ? super.getRequest().getData("id", int.class) : 0;

			ActivityLog log = this.repository.findById(id);
			isAuthorised = log != null;
			if (!isAuthorised) {
				super.getResponse().setAuthorised(isAuthorised);
				return;
			}

			isAuthorised = log.getFlightAssignment().getAssignee().getUserAccount().getId() == userAccountId;
		} catch (Exception e) {
			isAuthorised = false;
		}

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", Integer.TYPE);
		ActivityLog log = this.repository.findById(id);
		super.getBuffer().addData(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		Collection<Leg> legs = this.assignmentRepository.findLegsByCrew(userAccountId);
		SelectChoices legChoices = SelectChoices.from(legs, "flightCode", log.getLeg());

		dataset = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severity", "published");
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		Boolean canPublish = !log.getPublished() && log.getFlightAssignment().getPublished();
		dataset.put("canPublish", canPublish);

		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}
