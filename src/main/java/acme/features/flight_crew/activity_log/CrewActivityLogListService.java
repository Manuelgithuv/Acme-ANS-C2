
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
public class CrewActivityLogListService extends AbstractGuiService<FlightCrew, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ActivityLogRepository		repository;
	@Autowired
	private FlightAssignmentRepository	assignmentRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int flightCrewId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Collection<ActivityLog> logs = this.repository.findByFlightCrewId(flightCrewId);
		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		Collection<Leg> legs = this.assignmentRepository.findLegsByCrew(userAccountId);
		SelectChoices legChoices = SelectChoices.from(legs, "flightCode", log.getLeg());

		dataset = super.unbindObject(log, "registrationMoment", "incidentType", "severity", "published");
		super.addPayload(dataset, log, "description", "flightAssignment.duty", "leg.departureAirport", "leg.arrivalAirport", "leg.flight", "leg.aircraft", "leg.manager");

		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}

}
