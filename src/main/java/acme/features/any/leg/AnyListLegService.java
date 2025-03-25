package acme.features.any.leg;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.features.manager.leg.LegRepository;

@GuiService
public class AnyListLegService extends AbstractGuiService<Any, Leg> {
	
	@Autowired
	private LegRepository legRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		int flightId = super.getRequest().getData("flightId", int.class);

		List<Leg> flights = this.legRepository.findDistinctByFlight(flightId);

		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Leg leg) {

		Dataset dataset;

		dataset = super.unbindObject(leg, "flightCode", "scheduledDeparture", "scheduledArrival", "status", "hours", "manager.identity.fullName");

		super.getResponse().addData(dataset);

	}
	
	@Override
	public void unbind(final Collection<Leg> legs) {
		
		int flightId = super.getRequest().getData("flightId",int.class);
		super.getResponse().addGlobal("flightId", flightId);
	} 

}
