package acme.features.any.flight;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flight.Flight;

@GuiController
public class AnyFlightController extends AbstractGuiController<Any, Flight> {
	
	@Autowired
	private AnyListFlightService listService;
	
	@Autowired
	private AnyShowFlightService showService;
	
	@PostConstruct
	public void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
