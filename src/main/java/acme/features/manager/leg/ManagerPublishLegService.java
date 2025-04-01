
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.LegStatus;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftRepository;
import acme.entities.airport.Airport;
import acme.entities.airport.AirportRepository;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerPublishLegService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private LegRepository		legRepository;

	@Autowired
	private AirportRepository	airportRepository;

	@Autowired
	private AircraftRepository	aircraftRepository;


	@Override
	public void authorise() {
		boolean status;

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		int id = super.getRequest().getData("id", int.class);

		Leg leg = this.legRepository.findById(id);

		status = leg != null && leg.getManager().getId() == managerId && !leg.getFlight().isPublished() && !leg.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;

		int id;

		id = super.getRequest().getData("id", int.class);

		leg = this.legRepository.findById(id);

		super.getBuffer().addData(leg);

	}

	@Override
	public void bind(final Leg leg) {

		int aircraftId;
		Aircraft aircraft;

		int departureAirportId;
		Airport departureAirport;

		int arrivalAirportId;
		Airport arrivalAirport;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.aircraftRepository.findById(aircraftId);

		departureAirportId = super.getRequest().getData("departureAirport", int.class);
		departureAirport = this.airportRepository.findById(departureAirportId);

		arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		arrivalAirport = this.airportRepository.findById(arrivalAirportId);

		super.bindObject(leg, "flightCode", "scheduledDeparture", "scheduledArrival", "status", "hours");

		leg.setAircraft(aircraft);
		leg.setDepartureAirport(departureAirport);
		leg.setArrivalAirport(arrivalAirport);

	}

	@Override
	public void validate(final Leg leg) {

		Optional<Leg> existingLeg = this.legRepository.findByFlightCode(leg.getFlightCode());

		if (!existingLeg.isEmpty() && !leg.getFlightCode().equals(existingLeg.get().getFlightCode()))
			super.state(false, "flightCode", "manager.leg.flightCode.alreadyExists");

		boolean status;

		// Validar si los aeropuertos de leg son nulos antes de acceder a sus IDs
		Airport departureAirport = leg.getDepartureAirport() != null ? this.airportRepository.findById(leg.getDepartureAirport().getId()) : null;

		Airport arrivalAirport = leg.getArrivalAirport() != null ? this.airportRepository.findById(leg.getArrivalAirport().getId()) : null;

		// Validar si los aeropuertos encontrados son nulos antes de comparar IDs
		boolean areAirportsEquals = departureAirport != null && arrivalAirport != null &&

			departureAirport.getId() == arrivalAirport.getId();

		// Validar si scheduledDeparture y scheduledArrival son nulos antes de llamar a before()
		Date scheduledDeparture = leg.getScheduledDeparture();
		Date scheduledArrival = leg.getScheduledArrival();

		boolean isDepartureBeforeArrival = scheduledDeparture != null && scheduledArrival != null && scheduledDeparture.before(scheduledArrival);

		status = isDepartureBeforeArrival;

		if (areAirportsEquals)
			super.state(false, "*", "manager.leg.create.airports");

		super.state(status, "*", "manager.leg.create.dates");
	}

	@Override
	public void perform(final Leg leg) {
		leg.setPublished(true);
		this.legRepository.save(leg);

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset = this.buildDataset(leg);

		this.populateDatasetWithChoices(dataset, leg);

		super.getResponse().addData(dataset);
	}

	private Dataset buildDataset(final Leg leg) {
		return super.unbindObject(leg, "flightCode", "scheduledDeparture", "scheduledArrival", "status", "hours", "published");
	}

	private void populateDatasetWithChoices(final Dataset dataset, final Leg leg) {
		Collection<Airport> airports = this.airportRepository.findAllAirports();
		Collection<Aircraft> aircrafts = this.aircraftRepository.findAllActiveAircrafts();

		SelectChoices departureAirportChoices = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		SelectChoices arrivalAirportChoices = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		Aircraft aircraft = leg.getAircraft() == null || leg.getAircraft().getId() == 0 ? null : leg.getAircraft();
		SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", aircraft);
		SelectChoices statusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());

		dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());
		dataset.put("departureAirports", departureAirportChoices);
		dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalAirportChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("statuses", statusChoices);

	}

}
