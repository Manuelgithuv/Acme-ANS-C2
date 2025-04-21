
package acme.features.flight_crew.activity_log;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.activity_log.ActivityLog;
import acme.realms.FlightCrew;

@GuiController
public class CrewActivityLogController extends AbstractGuiController<FlightCrew, ActivityLog> {

	@Autowired
	private CrewActivityLogListService		list;
	@Autowired
	private CrewActivityLogShowService		show;
	@Autowired
	private CrewActivityLogCreateService	create;
	@Autowired
	private CrewActivityLogUpdateService	update;
	@Autowired
	private CrewActivityLogDeleteService	delete;
	@Autowired
	private CrewActivityLogPublishService	publish;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.list);
		super.addBasicCommand("show", this.show);
		super.addBasicCommand("create", this.create);
		super.addBasicCommand("update", this.update);
		super.addBasicCommand("delete", this.delete);
		super.addCustomCommand("publish", "update", this.publish);
	}
}
