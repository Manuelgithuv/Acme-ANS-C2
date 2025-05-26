
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
public class CrewActivityLogCreateService extends AbstractGuiService<FlightCrew, ActivityLog> {

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

		if (!super.getRequest().getMethod().equals("GET"))
			try {
				int userAccountId = super.getRequest().getPrincipal().getAccountId();

				Leg selectedLeg = super.getRequest().getData("leg", Leg.class);
				Boolean validLeg = selectedLeg == null ? true
					: this.assignmentRepository.findLegsByCrew(userAccountId).stream() //
						.filter(x -> x.isPublished()) //
						.anyMatch(x -> x.getId() == selectedLeg.getId());

				isAuthorised = validLeg;
			} catch (Exception e) {
				isAuthorised = false;
			}
		else
			isAuthorised = true;

		super.getResponse().setAuthorised(isAuthorised);
	}

	@Override
	public void load() {
		ActivityLog log = new ActivityLog();
		log.setPublished(false);
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

		status = log.getRegistrationMoment().before(log.getLeg().getScheduledArrival());
		if (status) {
			super.state(!status, "registrationMoment", "flight-crew.activity-log.constraint.log-registered-before-arrival", new Object[0]);
			return;
		}

		status = log.getSeverity() < 0 || log.getSeverity() > 10;
		if (status) {
			super.state(!status, "severity", "flight-crew.activity-log.constraint.invalid-severity-value", new Object[0]);
			return;
		}

		status = log.getIncidentType().length() <= 50;
		if (!status) {
			super.state(!status, "incidentType", "flight-crew.flight-assignment.constraint.too-long-incident-type", new Object[0]);
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
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;
		int userAccountId = super.getRequest().getPrincipal().getAccountId();

		dataset = super.unbindObject(log, "registrationMoment", "incidentType", "description", "severity");

		Collection<Leg> legs = this.assignmentRepository.findLegsByCrew(userAccountId).stream() //
			.filter(x -> x.isPublished()) // filtramos por publicados;
			.toList();
		SelectChoices legChoices = SelectChoices.from(legs, "flightCode", log.getLeg());
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
