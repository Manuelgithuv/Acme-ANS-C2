
package acme.features.flight_crew.activity_log;

import java.util.Collection;

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
public class CrewActivityLogUpdateService extends AbstractGuiService<FlightCrew, ActivityLog> {

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
		Boolean isAuthorised;
		int id;

		try {
			id = super.getRequest().hasData("id") ? super.getRequest().getData("id", int.class) : 0;

			ActivityLog log = this.repository.findById(id);
			isAuthorised = log != null;
			if (!isAuthorised) {
				super.getResponse().setAuthorised(isAuthorised);
				return;
			}

			FlightCrew user = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
			int userAccountId = super.getRequest().getPrincipal().getAccountId();

			isAuthorised = log.getFlightAssignment().getAssignee().getId() == user.getId() //
				&& !log.getPublished();
			if (!isAuthorised) {
				super.getResponse().setAuthorised(isAuthorised);
				return;
			}

			Leg selectedLeg = super.getRequest().getData("leg", Leg.class);
			Boolean validLeg = selectedLeg == null ? true
				: this.assignmentRepository.findLegsByCrew(userAccountId).stream() //
					.filter(x -> x.isPublished()) //
					.anyMatch(x -> x.getId() == selectedLeg.getId());

			isAuthorised = validLeg;

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
	public void bind(final ActivityLog log) {
		super.bindObject(log, "registrationMoment", "incidentType", "description", "severity", "leg");
	}

	@Override
	public void validate(final ActivityLog log) {
		Boolean status;

		// comprobamos que ningún atributo sea nulo
		status = log.getRegistrationMoment() == null || log.getIncidentType() == null || log.getDescription() == null || log.getSeverity() == null || log.getLeg() == null;
		if (status) {
			super.state(!status, "*", "flight-crew.activity-log.constraint.null-value", new Object[0]);
			return;
		}

		// assign assignment
		FlightCrew user = (FlightCrew) super.getRequest().getPrincipal().getActiveRealm();
		FlightAssignment assignment = this.assignmentRepository.findByAssigneeAndLeg(user, log.getLeg().getId());
		log.setFlightAssignment(assignment);

		status = log.getFlightAssignment() == null;
		if (status) {
			super.state(!status, "*", "flight-crew.activity-log.constraint.null-assignment", new Object[0]);
			return;
		}

		// registration moment
		status = log.getRegistrationMoment().before(log.getLeg().getScheduledArrival());
		if (status) {
			super.state(!status, "registrationMoment", "flight-crew.activity-log.constraint.log-registered-before-arrival", new Object[0]);
			return;
		}
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		int userAccountId = super.getRequest().getPrincipal().getAccountId();

		Collection<Leg> legs = this.assignmentRepository.findLegsByCrew(userAccountId).stream() //
			.filter(x -> x.isPublished()) // filtramos por publicados;
			.toList();
		SelectChoices legChoices = SelectChoices.from(legs, "flightCode", log.getLeg());
		Dataset dataset = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severity");
		dataset.put("legs", legChoices);

		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
