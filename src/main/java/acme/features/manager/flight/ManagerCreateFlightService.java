package acme.features.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerCreateFlightService extends AbstractGuiService<Manager, Flight>{
	
	@Autowired
	private FlightRepository flightRepository;
	
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	
	@Override
	public void load() {
		
		Flight flight;
		Manager manager;
		
		manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();
		
		flight = new Flight();
		flight.setPublished(false);
		flight.setManager(manager);
		super.getBuffer().addData(flight);
		
	}
	
	@Override
	public void bind(final Flight flight) {
		
		super.bindObject(flight, "tag","indication","cost","description");
		
		
	}
	
	@Override
	public void validate(final Flight flight) {
		;
	}
	
	@Override
	public void perform(final Flight flight) {
		
		this.flightRepository.save(flight);
		
	}
	
	@Override
	public void unbind(final Flight flight) {
		
		Dataset dataset;
		
		dataset = super.unbindObject(flight, "tag","indication","cost","published","description","scheduledDeparture","scheduledArrival","originCity"
			,"destinationCity","layovers");
		
		
		super.getResponse().addData(dataset);
		
	}

}
