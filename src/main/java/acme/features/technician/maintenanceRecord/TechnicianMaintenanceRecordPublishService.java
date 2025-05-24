
package acme.features.technician.maintenanceRecord;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.MoneyService;
import acme.datatypes.AircraftStatus;
import acme.datatypes.MaintenanceRecordStatus;
import acme.entities.aircraft.Aircraft;
import acme.entities.involves.Involves;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.features.technician.involves.TechnicianInvolvesRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianMaintenanceRecordPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private TechnicianMaintenanceRecordRepository	repository;

	@Autowired
	private TechnicianInvolvesRepository			repositoryInvolves;

	@Autowired
	private MoneyService							moneyService;


	// AbstractGuiService interface -------------------------------------------
	@Override
	public void authorise() {
		boolean exist;
		MaintenanceRecord maintenanceRecord;
		Technician technician;
		int id;

		id = super.getRequest().hasData("id") ? super.getRequest().getData("id", int.class) : 0;

		maintenanceRecord = this.repository.findMaintenanceRecordById(id);

		exist = maintenanceRecord != null;

		if (exist) {
			technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();
			if (technician.equals(maintenanceRecord.getTechnician()) && maintenanceRecord.isDraftMode())
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

		Collection<Involves> involvesAsociadas = this.repositoryInvolves.findAllInvolvesByMaintenanceRecordId(maintenanceRecord.getId());

		boolean todasSonPublicas = false;
		if (!involvesAsociadas.isEmpty())
			todasSonPublicas = involvesAsociadas.stream().allMatch(i -> !i.getTask().isDraftMode());

		super.state(maintenanceRecord.isDraftMode(), "*", "technician.maintenance-record.publish.is-not-in-draft-mode");

		if (!this.getBuffer().getErrors().hasErrors("aircraft") && maintenanceRecord.getAircraft() != null)
			super.state(maintenanceRecord.getAircraft().getStatus().equals(AircraftStatus.UNDER_MAINTENANCE), "*", "technician.maintenance-record.publish.is-not-aircraft-under-maintenance");

		super.state(!involvesAsociadas.isEmpty() && todasSonPublicas, "*", "technician.maintenance-record.publish.there-are-all-tasks-published");

		boolean currencyState = maintenanceRecord.getEstimatedCost() != null && this.moneyService.checkContains(maintenanceRecord.getEstimatedCost().getCurrency());

		if (!currencyState)
			super.state(currencyState, "estimatedCost", "manager.flight.invalid-currency");

		int id = super.getRequest().getData("id", int.class);

		MaintenanceRecord maintenanceRecord1 = this.repository.findMaintenanceRecordById(id);

		List<Aircraft> aircrafts = this.repository.findAircraftsUnderMaintenanceOrSpecific(AircraftStatus.UNDER_MAINTENANCE, maintenanceRecord1.getAircraft().getId());
		boolean aircraftValido = false;

		if (maintenanceRecord.getAircraft() != null)
			for (int i = 0; i < aircrafts.size() && !aircraftValido; i++)
				if (aircrafts.get(i).getId() == maintenanceRecord.getAircraft().getId())
					aircraftValido = true;

		if (!this.getBuffer().getErrors().hasErrors("aircraft"))
			super.state(aircraftValido, "aircraft", "technician.maintenance-record.form.error.not.aircraft.permitted", maintenanceRecord);
	}

	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		maintenanceRecord.setDraftMode(false);
		this.repository.save(maintenanceRecord);
	}

	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		SelectChoices choices;
		Collection<Aircraft> aircrafts;
		SelectChoices aircraft;
		MaintenanceRecord maintenanceRecord1;
		int id = super.getRequest().getData("id", int.class);

		maintenanceRecord1 = this.repository.findMaintenanceRecordById(id);

		Dataset dataset;
		aircrafts = this.repository.findAircraftsUnderMaintenanceOrSpecific(AircraftStatus.UNDER_MAINTENANCE, maintenanceRecord1.getAircraft().getId());
		choices = SelectChoices.from(MaintenanceRecordStatus.class, maintenanceRecord.getStatus());
		aircraft = SelectChoices.from(aircrafts, "id", maintenanceRecord.getAircraft() == null || maintenanceRecord.getAircraft().getId() == 0 || !aircrafts.contains(maintenanceRecord.getAircraft()) ? null : maintenanceRecord.getAircraft());

		dataset = super.unbindObject(maintenanceRecord, "status", "inspectionDueDate", "estimatedCost", "notes", "aircraft", "draftMode");

		dataset.put("status", choices.getSelected().getKey());
		dataset.put("status", choices);
		dataset.put("aircraft", aircraft.getSelected().getKey());
		dataset.put("aircraft", aircraft);

		super.getResponse().addData(dataset);
	}
}
