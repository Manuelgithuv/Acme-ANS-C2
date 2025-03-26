package acme.features.costumer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerShowFlightService extends AbstractGuiService<Manager, Flight>  {
	
	@Autowired
	private FlightRepository flightRepository;
	
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	
	@Override
	public void load() {
		
		Flight flight;
		
		int id;

		id = super.getRequest().getData("id", int.class);
		
		flight = flightRepository.findById(id);
		
		super.getBuffer().addData(flight);
		
	}
	
	@Override
	public void unbind(final Flight flight) {
		
		Dataset dataset;
		
		dataset = super.unbindObject(flight, "tag","indication","cost","published","description","scheduledDeparture","scheduledArrival","originCity"
			,"destinationCity","layovers");
		
		
		super.getResponse().addData(dataset);
		
	}

}
