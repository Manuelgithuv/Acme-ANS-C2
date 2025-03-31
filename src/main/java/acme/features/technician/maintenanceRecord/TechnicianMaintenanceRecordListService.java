
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordListService extends AbstractGuiService<Technician, MaintenanceRecord> {

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

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		maintenanceRecord = this.repository.findAllMaintenanceRecordByTechnicianId(technicianId);

		super.getBuffer().addData(maintenanceRecord);
	}

	private boolean predicado(final MaintenanceRecord mr, final int technicianId) {
		boolean result = !mr.isDraftMode();
		if (mr.isDraftMode() && mr.getTechnician().getId() == technicianId)
			result = true;
		return result;
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;

		dataset = super.unbindObject(maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost", "draftMode");

		super.addPayload(dataset, maintenanceRecord, "moment", "status", "inspectionDueDate", "estimatedCost", "draftMode");

		super.getResponse().addData(dataset);
	}

}
