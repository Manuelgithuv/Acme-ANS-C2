
package acme.features.any.maintenanceRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@GuiController
public class AnyMaintenanceRecordController extends AbstractGuiController<Any, MaintenanceRecord> {

	//Internal state --------------------------------------------------------------

	@Autowired
	private AnyMaintenanceRecordListService	listService;

	@Autowired
	private AnyMaintenanceRecordShowService	showService;

	//Constructors ----------------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
