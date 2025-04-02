
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;
import acme.features.technician.maintenanceRecord.TechnicianMaintenanceRecordRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskListByMaintenanceRecordService extends AbstractGuiService<Technician, Task> {

	//Internal state ---------------------------------------------

	@Autowired
	private TechnicianTaskRepository				repository;

	@Autowired
	private TechnicianMaintenanceRecordRepository	maintenanceRepository;

	//AbstractGuiService interface -------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Task> task;
		int maintenanceRecordId;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);

		task = this.repository.findTasksByMaintenanceRecordId(maintenanceRecordId);

		super.getBuffer().addData(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");

		super.addPayload(dataset, task, "type", "description", "priority", "estimatedDuration", "draftMode");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Task> task) {

		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		MaintenanceRecord maintenanceRecord = this.maintenanceRepository.findMaintenanceRecordById(maintenanceRecordId);
		boolean isMaintenanceRecordDraftMode;
		isMaintenanceRecordDraftMode = task != null && maintenanceRecord.isDraftMode();
		super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);
		super.getResponse().addGlobal("isMaintenanceRecordDraftMode", isMaintenanceRecordDraftMode);
	}

}
