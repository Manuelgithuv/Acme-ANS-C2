package acme.features.manager.flight;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiController
public class ManagerFlightController extends AbstractGuiController<Manager, Flight> {
	
	@Autowired
	private ManagerListFlightService listService;
	
	@Autowired
	private ManagerShowFlightService showService;
	
	@Autowired
	private ManagerCreateFlightService createFlightService;
	
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", createFlightService);
	}

}
