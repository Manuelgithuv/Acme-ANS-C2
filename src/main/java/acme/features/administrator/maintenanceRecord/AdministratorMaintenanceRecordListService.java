
package acme.features.administrator.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.features.technician.maintenanceRecord.TechnicianMaintenanceRecordRepository;

@GuiService
public class AdministratorMaintenanceRecordListService extends AbstractGuiService<Administrator, MaintenanceRecord> {

	//Internal state ---------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository repository;

	//AbstractGuiService interface -------------------------------


	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<MaintenanceRecord> maintenanceRecord;
		int technicianId;

		maintenanceRecord = this.repository.findAllMaintenanceRecords();

		super.getBuffer().addData(maintenanceRecord.stream().filter(m -> !m.isDraftMode()).toList());
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;

		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost");

		super.addPayload(dataset, maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost");

		super.getResponse().addData(dataset);
	}
}
