
package acme.features.flight_crew.activity_log;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.features.manager.leg.LegRepository;
import acme.realms.FlightCrew;

@GuiService
public class FlightCrewShowActivityLogService extends AbstractGuiService<FlightCrew, ActivityLog> {

	@Autowired
	private ActivityLogRepository	repository;
	@Autowired
	private LegRepository			legRepository;


	public FlightCrewShowActivityLogService() {
	}

	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", Integer.TYPE);
		ActivityLog log = this.repository.findById(id);
		super.getBuffer().addData(log);
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
		super.getResponse().addData(dataset);
	}
}
