package acme.features.manager.leg;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiController
public class ManagerLegController extends AbstractGuiController<Manager, Leg> {
	
	@Autowired
	private ManagerListLegService listService;
	
	@Autowired
	private ManagerShowLegService showService;
	
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
