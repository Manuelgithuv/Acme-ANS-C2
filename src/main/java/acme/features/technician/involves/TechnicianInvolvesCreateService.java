
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.involves.Involves;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;
import acme.features.technician.maintenanceRecord.TechnicianMaintenanceRecordRepository;
import acme.features.technician.task.TechnicianTaskRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvesCreateService extends AbstractGuiService<Technician, Involves> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianInvolvesRepository			repository;

	@Autowired
	private TechnicianMaintenanceRecordRepository	maintenanceRepository;

	@Autowired
	private TechnicianTaskRepository				taskRepository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		MaintenanceRecord maintenanceRecord = this.maintenanceRepository.findMaintenanceRecordById(maintenanceRecordId);
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		Collection<Task> tasks = this.repository.findValidTasksToAdd(maintenanceRecord, technician);
		boolean taskExist = true;

		if (!super.getRequest().getMethod().equals("GET")) {

			int taskId = super.getRequest().getData("task", int.class);
			Task task = this.taskRepository.findTaskById(taskId);
			if (taskId != 0 && task == null)
				taskExist = false;
			if (task != null && !tasks.contains(task))
				taskExist = false;
		}

		if (maintenanceRecord != null && maintenanceRecord.getTechnician().getId() == super.getRequest().getPrincipal().getActiveRealm().getId() && taskExist)
			super.getResponse().setAuthorised(maintenanceRecord.isDraftMode());
	}

	@Override
	public void load() {
		Involves involves;
		Integer maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		involves = new Involves();
		involves.setMaintenanceRecord(maintenanceRecord);
		super.getBuffer().addData(involves);
	}

	@Override
	public void bind(final Involves involves) {
		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		MaintenanceRecord maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);
		int taskId = super.getRequest().getData("task", int.class);
		Task task = this.taskRepository.findTaskById(taskId);

		involves.setMaintenanceRecord(maintenanceRecord);
		involves.setTask(task);
	}

	@Override
	public void validate(final Involves involves) {
		int taskId = super.getRequest().getData("task", int.class);
		Task task = this.taskRepository.findTaskById(taskId);
		if (task != null)
			super.state(!task.isDraftMode() || task.getTechnician().getId() == super.getRequest().getPrincipal().getActiveRealm().getId(), "*", "technician.involves.form.error.invalid-task");

	}

	@Override
	public void perform(final Involves involves) {
		this.repository.save(involves);
	}

	@Override
	public void unbind(final Involves involves) {
		Technician technician;
		Collection<Task> tasks;
		int maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;
		SelectChoices choices;
		Dataset dataset;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(maintenanceRecordId);

		tasks = this.repository.findValidTasksToAdd(maintenanceRecord, technician);
		choices = SelectChoices.from(tasks, "id", involves.getTask());

		dataset = super.unbindObject(involves, "maintenanceRecord");
		dataset.put("maintenanceRecordId", involves.getMaintenanceRecord().getId());
		dataset.put("task", choices.getSelected().getKey());
		dataset.put("tasks", choices);
		dataset.put("aircraftRegistrationNumber", involves.getMaintenanceRecord().getAircraft().getRegistrationNumber());

		super.getResponse().addData(dataset);

	}
}
