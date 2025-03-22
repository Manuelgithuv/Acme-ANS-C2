package acme.features.manager.leg;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftRepository;
import acme.entities.airport.Airport;
import acme.entities.airport.AirportRepository;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerPublishLegService extends AbstractGuiService<Manager, Leg>{
	
	@Autowired
	private LegRepository legRepository;
	
	@Autowired
	private AirportRepository airportRepository;
	
	
	
	@Autowired
	private AircraftRepository aircraftRepository;
	

	@Override
	public void authorise() {
		boolean status;
		
		Leg leg;
		
		
		int id;

		id = super.getRequest().getData("id", int.class);
		
		leg = legRepository.findById(id);
		
		status = !leg.isPublished();
		
		super.getResponse().setAuthorised(status);
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
	public void bind(final Leg leg) {
		
		
		
		int aircraftId;
		Aircraft aircraft;
		
		int departureAirportId;
		Airport departureAirport;
		
		int arrivalAirportId;
		Airport arrivalAirport;
		
		
		
		aircraftId = super.getRequest().getData("aircraft",int.class);
		aircraft = this.aircraftRepository.findById(aircraftId);
		
		departureAirportId = super.getRequest().getData("departureAirport",int.class);
		departureAirport = this.airportRepository.findById(departureAirportId);
		
		arrivalAirportId = super.getRequest().getData("arrivalAirport",int.class);
		arrivalAirport = this.airportRepository.findById(arrivalAirportId);
		
		super.bindObject(leg, "flightCode","scheduledDeparture","scheduledArrival","status","hours");
		
		leg.setAircraft(aircraft);
		leg.setDepartureAirport(departureAirport);
		leg.setArrivalAirport(arrivalAirport);
		
		
	}
	
	@Override
	public void validate(final Leg leg) {
		boolean status;
		Airport departureAirport = this.airportRepository.findById(leg.getDepartureAirport().getId());
		Airport arrivalAirport = this.airportRepository.findById(leg.getArrivalAirport().getId());
		boolean areAirportsEquals = departureAirport!=null && arrivalAirport!=null && departureAirport.getId()==arrivalAirport.getId();
		
		Date scheduledDeparture = leg.getScheduledDeparture();
		Date scheduledArrival = leg.getScheduledArrival();
		boolean isDepartureBeforeArrival = scheduledDeparture!=null && scheduledArrival!=null && scheduledDeparture.before(scheduledArrival);
		
		status = !areAirportsEquals && isDepartureBeforeArrival;
		
		
		super.state(status, "*", "manager.leg.create.dates-and-airports");
	}
	
	@Override
	public void perform(final Leg leg) {
		leg.setPublished(true);
		this.legRepository.save(leg);
		
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
	    

	    SelectChoices departureAirportChoices = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
	    SelectChoices arrivalAirportChoices = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
	    SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
	    

	    dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());
	    dataset.put("departureAirports", departureAirportChoices);
	    dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());
	    dataset.put("arrivalAirports", arrivalAirportChoices);
	    dataset.put("aircraft", aircraftChoices.getSelected().getKey());
	    dataset.put("aircrafts", aircraftChoices);
	    
	}

}
