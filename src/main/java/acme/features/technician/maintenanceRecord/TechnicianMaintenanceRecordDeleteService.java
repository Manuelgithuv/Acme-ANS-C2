
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.MaintenanceRecordStatus;
import acme.entities.aircraft.Aircraft;
import acme.entities.involves.Involves;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.features.technician.involves.TechnicianInvolvesRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordDeleteService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository	repository;

	@Autowired
	private TechnicianInvolvesRepository			repositoryInvolves;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean exist;
		MaintenanceRecord maintenanceRecord;
		Technician technician;
		int id;

		id = super.getRequest().getData("id", int.class);

		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		exist = maintenanceRecord != null;

		if (exist) {
			technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
			if (technician.equals(maintenanceRecord.getTechnician()) && !maintenanceRecord.isDraftMode())
				super.getResponse().setAuthorised(true);
			else
				super.getResponse().setAuthorised(false);
		}
	}

	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {
		super.bindObject(maintenanceRecord, "status", "inspectionDueDate", "estimatedCost", "notes", "aircraft");
	}

	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {

		if (!maintenanceRecord.isDraftMode())
			super.state(!maintenanceRecord.isDraftMode(), "*", "technician.maintenance-record.publish.is-not-in-draft-mode");
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		Collection<Involves> involvesAsociadas = this.repositoryInvolves.findAllInvolvesByMaintenanceRecordId(maintenanceRecord.getId());

		this.repositoryInvolves.deleteAll(involvesAsociadas);

		this.repository.delete(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		SelectChoices choices;
		Collection<Aircraft> aircrafts;
		SelectChoices aircraft;

		Dataset dataset;
		aircrafts = this.repository.findAllAircrafts();
		choices = SelectChoices.from(MaintenanceRecordStatus.class, maintenanceRecord.getStatus());
		aircraft = SelectChoices.from(aircrafts, "id", maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "status", "inspectionDueDate", "estimatedCost", "notes", "aircraft");

		dataset.put("status", choices.getSelected().getKey());
		dataset.put("aircraft", aircraft.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
