
package acme.features.flight_crew.activity_log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewListActivityLogService extends AbstractGuiService<FlightCrew, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	public FlightCrewListActivityLogService() {
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int flightCrewId = super.getRequest().getPrincipal().getActiveRealm().getId();
		List<ActivityLog> logs = this.repository.findByFlightCrewId(flightCrewId);
		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset = super.unbindObject(log, new String[] {
			"registrationMoment", "incidentType", "severity", "leg.flightCode"
		});
		super.addPayload(dataset, log, new String[] {
			"description", "flightAssignment.duty", "leg.departureAirport", "leg.arrivalAirport", "leg.flight", "leg.aircraft", "leg.manager"
		});
		super.getResponse().addData(dataset);
	}
}
