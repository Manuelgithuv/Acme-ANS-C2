
package acme.features.flight_crew.flight_assignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flight_assignment.FlightAssignment;
import acme.realms.FlightCrew;

@GuiController
public class FlightCrewFlightAssignmentController extends AbstractGuiController<FlightCrew, FlightAssignment> {

	@Autowired
	private FlightCrewListCompletedFlightAssignmentService	listCompleted;
	@Autowired
	private FlightCrewListPlannedFlightAssignmentService	listPlanned;
	@Autowired
	private FlightCrewShowFlightAssignmentService			show;
	@Autowired
	private FlightCrewCreateFlightAssignmentService			create;
	@Autowired
	private FlightCrewUpdateFlightAssignmentService			update;
	@Autowired
	private FlightCrewDeleteFlightAssignmentService			delete;
	@Autowired
	private FlightCrewPublishFlightAssignmentService		publish;


	public FlightCrewFlightAssignmentController() {
	}

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
