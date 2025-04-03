
package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.TaskType;
import acme.entities.involves.Involves;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;
import acme.features.technician.involves.TechnicianInvolvesRepository;
import acme.features.technician.maintenanceRecord.TechnicianMaintenanceRecordRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianTaskCreateByMaintenanceRecordService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository				repository;

	@Autowired
	private TechnicianMaintenanceRecordRepository	maintenanceRepository;

	@Autowired
	private TechnicianInvolvesRepository			involvesRepository;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {

		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		MaintenanceRecord maintenanceRecord = this.maintenanceRepository.findMaintenanceRecordById(maintenanceRecordId);

		super.getResponse().setAuthorised(maintenanceRecord != null && super.getRequest().getPrincipal().getActiveRealm().getId() == maintenanceRecord.getTechnician().getId() && maintenanceRecord.isDraftMode());
	}

	@Override
	public void load() {
		Task task;
		Technician technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		task = new Task();
		task.setDraftMode(true);
		task.setTechnician(technician);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
	}

	@Override
	public void validate(final Task task) {

		if (!this.getBuffer().getErrors().hasErrors("type"))
			super.state(task.getType() != null, "type", "technician.task.form.error.noType", task);

		if (!this.getBuffer().getErrors().hasErrors("description") && task.getDescription() != null)
			super.state(task.getDescription().length() <= 255, "description", "technician.task.form.error.description", task);

		if (!this.getBuffer().getErrors().hasErrors("priority"))
			super.state(0 <= task.getPriority() && task.getPriority() <= 10, "priority", "technician.task.form.error.priority", task);

		if (!this.getBuffer().getErrors().hasErrors("estimatedDuration"))
			super.state(0 <= task.getEstimatedDuration() && task.getEstimatedDuration() <= 10000, "estimatedDuration", "technician.task.form.error.estimatedDuration", task);
	}

	@Override
	public void perform(final Task task) {
		Involves mainInvolvesTask = new Involves();

		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		MaintenanceRecord maintenanceRecord = this.maintenanceRepository.findMaintenanceRecordById(maintenanceRecordId);

		assert maintenanceRecord != null;

		mainInvolvesTask.setMaintenanceRecord(maintenanceRecord);
		mainInvolvesTask.setTask(task);

		this.repository.save(task);
		this.involvesRepository.save(mainInvolvesTask);
	}

	@Override
	public void unbind(final Task task) {
		SelectChoices choices;

		Dataset dataset;
		choices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");

		dataset.put("type", choices.getSelected().getKey());
		dataset.put("type", choices);

		dataset.put("maintenanceRecordId", super.getRequest().getData("maintenanceRecordId", int.class));

		super.getResponse().addData(dataset);
	}

}
