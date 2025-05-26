package acme.features.manager.airline;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.aircraft.Aircraft;
import acme.realms.Manager;

@GuiController
public class ManagerAirlineController extends AbstractGuiController<Manager, Aircraft>{
	
	@Autowired
	private ManagerAirlineListService listService;
	
	@PostConstruct
	protected void initialise() {
		
		super.addBasicCommand("list", this.listService);
		
	}

}
