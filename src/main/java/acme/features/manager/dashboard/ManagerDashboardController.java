package acme.features.manager.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.ManagerDashboard;
import acme.realms.Manager;

@GuiController
public class ManagerDashboardController extends AbstractGuiController<Manager, ManagerDashboard> {
	
	@Autowired
	private ManagerShowDashboardService showService;
	
	@PostConstruct
	public void initialise() {
		super.addBasicCommand("show", this.showService);
	}

}
