package acme.features.manager.flight;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerListFlightService extends AbstractGuiService<Manager, Flight> {
	
	@Autowired
	private FlightRepository flightRepository;
	
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}
	
	
	@Override
	public void load() {
		
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		
		List<Flight> flights = flightRepository.findFlightsByManagerId(managerId);
		
		super.getBuffer().addData(flights);
	}
	
	@Override
	public void unbind(final Flight flight) {
		
		Dataset dataset;
		
		dataset = super.unbindObject(flight, "tag","indication","cost","published","description","manager.identity.fullName");
		
		super.getResponse().addData(dataset);
		
	}

}
