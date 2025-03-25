
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.entities.task.Task;
import acme.realms.Technician;

public class TechnicianTaskShowService extends AbstractGuiService<Technician, Task> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianTaskRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Task> tasks;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		tasks = this.repository.findTasksBytechnicianId(technicianId);

		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "ticker", "title", "deadline");
		super.addPayload(dataset, task, "description", "moreInfo", "contractor.name", //
			"employer.identity.fullName", "employer.area", "employer.sector");

		super.getResponse().addData(dataset);
	}

}
