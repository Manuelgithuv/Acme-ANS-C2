
package acme.features.flight_crew.flight_assignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.entities.flight_assignment.FlightAssignment;
import acme.realms.FlightCrew;

public class CrewAssignmentController extends AbstractGuiController<FlightCrew, FlightAssignment> {

	@Autowired
	private CrewAssignmentListCompletedService	listCompleted;
	@Autowired
	private CrewAssignmentListPlannedService	listPlanned;
	@Autowired
	private CrewAssignmentShowService			show;
	@Autowired
	private CrewAssignmentCreateService			create;
	@Autowired
	private CrewAssignmentUpdateService			update;
	@Autowired
	private CrewAssignmentDeleteService			delete;
	@Autowired
	private CrewAssignmentPublishService		publish;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.show);
		super.addBasicCommand("create", this.create);
		super.addBasicCommand("update", this.update);
		super.addBasicCommand("delete", this.delete);
		super.addCustomCommand("list-completed", "list", this.listCompleted);
		super.addCustomCommand("list-planned", "list", this.listPlanned);
		super.addCustomCommand("publish", "update", this.publish);
	}

}
