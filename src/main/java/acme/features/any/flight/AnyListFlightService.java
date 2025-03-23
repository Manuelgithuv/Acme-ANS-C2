package acme.features.any.flight;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.features.manager.flight.FlightRepository;

@GuiService
public class AnyListFlightService extends AbstractGuiService<Any, Flight>{
	@Autowired
	private FlightRepository flightRepository;
	
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	
	
	@Override
	public void load() {
		
		
		
		List<Flight> flights = flightRepository.findPublicFlights();
		
		super.getBuffer().addData(flights);
	}
	
	@Override
	public void unbind(final Flight flight) {
		
		Dataset dataset;
		
		dataset = super.unbindObject(flight, "tag","indication","cost","description","manager.identity.fullName");
		
		super.getResponse().addData(dataset);
		
	}

}
