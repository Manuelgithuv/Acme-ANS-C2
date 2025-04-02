
package acme.features.any.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;
import acme.features.technician.task.TechnicianTaskRepository;

@GuiService
public class AnyTaskListService extends AbstractGuiService<Any, Task> {

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

		maintenanceRecordId = super.getRequest().getData("id", int.class);

		task = this.repository.findTasksByMaintenanceRecordId(maintenanceRecordId);

		super.getBuffer().addData(task.stream().filter(t -> !t.isDraftMode()).toList());
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");

		super.addPayload(dataset, task, "type", "description", "priority", "estimatedDuration", "draftMode");

		super.getResponse().addData(dataset);
	}

}
