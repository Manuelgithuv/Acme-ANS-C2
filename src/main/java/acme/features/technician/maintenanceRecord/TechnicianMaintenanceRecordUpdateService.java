
package acme.features.technician.maintenanceRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.MoneyService;
import acme.datatypes.MaintenanceRecordStatus;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordUpdateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository	repository;

	@Autowired
	private MoneyService							moneyService;


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
			if (technician.equals(maintenanceRecord.getTechnician()) && maintenanceRecord.isDraftMode())
				super.getResponse().setAuthorised(true);
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

		if (!this.getBuffer().getErrors().hasErrors("status"))
			super.state(maintenanceRecord.getStatus() != null, "status", "technician.maintenance-record.form.error.noStatus", maintenanceRecord);

		if (!this.getBuffer().getErrors().hasErrors("nextInspectionDate") && maintenanceRecord.getInspectionDueDate() != null)
			super.state(maintenanceRecord.getInspectionDueDate().compareTo(maintenanceRecord.getMoment()) > 0, "inspectionDueDate", "technician.maintenance-record.form.error.inspectionDueDate", maintenanceRecord);

		if (!this.getBuffer().getErrors().hasErrors("estimatedCost") && maintenanceRecord.getEstimatedCost() != null)
			super.state(0.00 <= maintenanceRecord.getEstimatedCost().getAmount() && maintenanceRecord.getEstimatedCost().getAmount() <= 1000000.00, "estimatedCost", "technician.maintenance-record.form.error.estimatedCost", maintenanceRecord);

		if (!this.getBuffer().getErrors().hasErrors("notes") && maintenanceRecord.getNotes() != null)
			super.state(maintenanceRecord.getNotes().length() <= 255, "notes", "technician.maintenance-record.form.error.notes", maintenanceRecord);

		if (!this.getBuffer().getErrors().hasErrors("aircraft") && maintenanceRecord.getAircraft() != null)
			super.state(this.repository.findAllAircrafts().contains(maintenanceRecord.getAircraft()), "aircraft", "technician.maintenance-record.form.error.aircraft", maintenanceRecord);

		boolean currencyState = maintenanceRecord.getEstimatedCost() != null && this.moneyService.checkContains(maintenanceRecord.getEstimatedCost().getCurrency());

		if (!currencyState)
			super.state(currencyState, "estimatedCost", "technician.maintenance-record.invalid-currency");
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		assert maintenanceRecord != null;

		this.repository.save(maintenanceRecord);
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

		dataset = super.unbindObject(maintenanceRecord, "status", "inspectionDueDate", "estimatedCost", "notes", "aircraft", "draftMode");

		dataset.put("status", choices.getSelected().getKey());
		dataset.put("status", choices);
		dataset.put("aircraft", aircraft.getSelected().getKey());
		dataset.put("aircraft", aircraft);

		super.getResponse().addData(dataset);
	}

}
