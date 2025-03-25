package acme.features.manager.leg;

import java.util.Collection;
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
public class ManagerShowLegService extends  AbstractGuiService<Manager, Leg> {
	
	@Autowired
	private LegRepository legRepository;
	
	@Autowired
	private AirportRepository airportRepository;
	
	@Autowired
	private FlightRepository flightRepository;
	
	@Autowired
	private AircraftRepository aircraftRepository;
	
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
		
		
	}
	
	@Override
	public void load() {
		
		Leg leg;
		
		
		int id;

		id = super.getRequest().getData("id", int.class);
		
		leg = legRepository.findById(id);
		
		super.getBuffer().addData(leg);
		
	}
	
	@Override
	public void unbind(final Leg leg) {
	    Dataset dataset = buildDataset(leg);
	    
	    populateDatasetWithChoices(dataset, leg);
	    
	    super.getResponse().addData(dataset);
	}

	private Dataset buildDataset(final Leg leg) {
	    return super.unbindObject(leg, "flightCode", "scheduledDeparture", "scheduledArrival","status", "hours","published");
	}

	private void populateDatasetWithChoices(Dataset dataset, final Leg leg) {
	    Collection<Airport> airports = airportRepository.findAllAirports();
	    Collection<Aircraft> aircrafts = aircraftRepository.findAllAircrafts();
	    Collection<Flight> flights = flightRepository.findFlightsByManagerId(leg.getManager().getId());

	    SelectChoices departureAirportChoices = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
	    SelectChoices arrivalAirportChoices = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
	    SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
	    SelectChoices flightChoices = SelectChoices.from(flights, "id", leg.getFlight());

	    dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());
	    dataset.put("departureAirports", departureAirportChoices);
	    dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());
	    dataset.put("arrivalAirports", arrivalAirportChoices);
	    dataset.put("aircraft", aircraftChoices.getSelected().getKey());
	    dataset.put("aircrafts", aircraftChoices);
	    dataset.put("flight", flightChoices.getSelected().getKey());
	    dataset.put("flights", flightChoices);
	}


}
