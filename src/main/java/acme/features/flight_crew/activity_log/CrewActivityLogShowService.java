
package acme.features.flight_crew.activity_log;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activity_log.ActivityLog;
import acme.features.manager.leg.LegRepository;
import acme.realms.FlightCrew;

@GuiService
public class CrewActivityLogShowService extends AbstractGuiService<FlightCrew, ActivityLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ActivityLogRepository	repository;
	@Autowired
	private LegRepository			legRepository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean isAuthorised;
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
		//SelectChoices legChoices = SelectChoices.from(this.legRepository.findAllLegs(), "flightCode", log.getLeg());
		Dataset dataset = super.unbindObject(log, new String[] {
			"registrationMoment", "incidentType", "description", "severity"
		});
		dataset.put("confirmation", false);
		dataset.put("readonly", true);
		//dataset.put("legs", legChoices);
		super.getResponse().addData(dataset);
	}

}
