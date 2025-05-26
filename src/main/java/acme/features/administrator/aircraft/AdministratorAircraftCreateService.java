
package acme.features.administrator.aircraft;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AircraftStatus;
import acme.datatypes.DisabledAircrafts;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftRepository;
import acme.entities.airline.Airline;
import acme.features.administrator.airline.AdministratorAirlineRepository;

@GuiService
public class AdministratorAircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AircraftRepository				repository;
	@Autowired
	private AdministratorAirlineRepository	airlineRepository;


	@Override
	public void authorise() {
		
		boolean entitiesExist = true;
		if (!super.getRequest().getMethod().equals("GET")) {

			int airlineId = super.getRequest().getData("airline", int.class);
			if (airlineId != 0 && this.airlineRepository.findById(airlineId) == null)
				entitiesExist = false;
		}
		super.getResponse().setAuthorised(entitiesExist);
	}

	@Override
	public void load() {
		Aircraft aircraft;

		aircraft = new Aircraft();

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.airlineRepository.findAirlineById(airlineId);
		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		aircraft.setAirline(airline);
	}

	@Override
	public void validate(final Aircraft aircraft) {
		Collection<Aircraft> aircrafts = this.repository.findAllAircrafts();
		if(aircraft.getRegistrationNumber()!=null) {
			boolean regRepeated = aircrafts.stream().anyMatch(a->a.getRegistrationNumber().equals(aircraft.getRegistrationNumber()));
			super.state(!regRepeated,"registrationNumber","acme.validation.repeatedRegNumber");
		}
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(AircraftStatus.class, aircraft.getStatus());
		Collection<Airline> airlines = this.airlineRepository.findAllAirlines();
		Airline airline = aircraft.getAirline() == null || aircraft.getAirline().getId() == 0 ? null : aircraft.getAirline();
		SelectChoices airlinesChoices = SelectChoices.from(airlines, "name", airline);

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "status", "details");
		dataset.put("confirmation", true);
		dataset.put("readonly", false);
		dataset.put("status", choices);
		dataset.put("airline", airlinesChoices.getSelected().getKey());
		dataset.put("airlines", airlinesChoices);

		List<Integer> idDisabledAircrafts = DisabledAircrafts.getAll();
		//Note: if you see this code, this was made this way to prevent already implemented test from crashing

		Boolean disabled = idDisabledAircrafts.contains(aircraft.getId());
		dataset.put("disabled", disabled);

		super.getResponse().addData(dataset);
	}

}
