
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftRepository;
import acme.entities.airport.Airport;
import acme.entities.airport.AirportRepository;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.features.manager.flight.FlightRepository;
import acme.realms.Manager;

@GuiService
public class ManagerCreateLegService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private LegRepository		legRepository;

	@Autowired
	private AirportRepository	airportRepository;

	@Autowired
	private FlightRepository	flightRepository;

	@Autowired
	private AircraftRepository	aircraftRepository;


	@Override
	public void authorise() {

		boolean status;

		int flightId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.flightRepository.findById(flightId);

		Manager manager;

		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		status = !flight.isPublished() && manager.getId() == flight.getManager().getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		Manager manager;

		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		int flightId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.flightRepository.findById(flightId);
		leg = new Leg();
		leg.setPublished(false);
		leg.setManager(manager);
		leg.setFlight(flight);
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
		if (leg == null) {
			super.state(false, "*", "manager.leg.create.null-leg");
			return;
		}
		if (leg.getFlight() == null) {
			super.state(false, "flight", "manager.leg.create.null-flight");
			return;
		}

		if (leg.getAircraft() == null) {
			super.state(false, "aircraft", "manager.leg.create.null-aircraft");
			return;
		}
		
		Optional<Leg> existingLeg = legRepository.findByFlightCode(leg.getFlightCode());
		
		if(!existingLeg.isEmpty()) {
			super.state(false, "flightCode", "manager.leg.flightCode.alreadyExists");
		}

		boolean status;

		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		if (leg.getManager().getId() != manager.getId())
			super.state(false, "manager", "leg.manager.is.not.logged-manager");
		;

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

		if (!areAirportsEquals)
			super.state(false, "*", "manager.leg.create.airports");

		super.state(status, "*", "manager.leg.create.dates");
	}

	@Override
	public void perform(final Leg leg) {
		this.legRepository.save(leg);

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset = this.buildDataset(leg);
		dataset.put("flightId", super.getRequest().getData("flightId", int.class));

		this.populateDatasetWithChoices(dataset, leg);

		super.getResponse().addData(dataset);
	}

	private Dataset buildDataset(final Leg leg) {
		return super.unbindObject(leg, "flightCode", "scheduledDeparture", "scheduledArrival", "status", "hours", "published");
	}

	private void populateDatasetWithChoices(final Dataset dataset, final Leg leg) {
		Collection<Airport> airports = this.airportRepository.findAllAirports();
		Collection<Aircraft> aircrafts = this.aircraftRepository.findAllAircrafts();

		SelectChoices departureAirportChoices = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		SelectChoices arrivalAirportChoices = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		Aircraft aircraft = leg.getAircraft() == null || leg.getAircraft().getId() == 0 ? null : leg.getAircraft();
		SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", aircraft);

		dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());
		dataset.put("departureAirports", departureAirportChoices);
		dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalAirportChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

	}

}
