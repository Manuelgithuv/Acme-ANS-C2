
package acme.features.administrator.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;
import acme.features.technician.task.TechnicianTaskRepository;

@GuiService
public class AdministratorTaskListService extends AbstractGuiService<Administrator, Task> {

	//Internal state ---------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

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

		super.getBuffer().addData(task.stream().filter(t -> !t.isDraftMode()).toList());
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Task> task) {

		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);
	}

}
