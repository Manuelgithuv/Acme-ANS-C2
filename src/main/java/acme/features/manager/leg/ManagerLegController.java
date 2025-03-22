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
	
	@Autowired
	private ManagerCreateLegService createService;
	
	@Autowired
	private ManagerUpdateLegService updateService;
	
	@Autowired
	private ManagerDeleteLegService deleteService;
	
	@Autowired
	private ManagerPublishLegService publishService;
	
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
