package acme.features.manager.airline;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftRepository;
import acme.realms.Manager;

@GuiService
public class ManagerAirlineListService extends AbstractGuiService<Manager, Aircraft>{
	
	@Autowired
	private AircraftRepository repository;
	
	@Override
	public void authorise() {

		super.getResponse().setAuthorised(true);
	}
	
	@Override
	public void load() {
		
		Collection<Aircraft> aircrafts = repository.findAllAircrafts();
		
		super.getBuffer().addData(aircrafts);
		
	}
	
	@Override
	public void unbind(final Aircraft aircraft) {
		
		Dataset dataset;
		
		dataset = super.unbindObject(aircraft,"registrationNumber","airline.iataCode");
		
		super.getResponse().addData(dataset);
		
	}

}
