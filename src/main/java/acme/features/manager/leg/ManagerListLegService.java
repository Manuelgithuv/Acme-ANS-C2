
package acme.features.manager.leg;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.features.manager.flight.FlightRepository;
import acme.realms.Manager;

@GuiService
public class ManagerListLegService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private LegRepository		legRepository;

	@Autowired
	private FlightRepository	flightRepository;


	@Override
	public void authorise() {
		boolean status;

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		int flightId = super.getRequest().getData("flightId", int.class);

		Flight flight = this.flightRepository.findById(flightId);

		status = flight != null && flight.getManager().getId() == managerId;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		int flightId = super.getRequest().getData("flightId", int.class);

		List<Leg> legs = this.legRepository.findDistinctByFlight(flightId);

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {

		Dataset dataset;

		dataset = super.unbindObject(leg, "flightCode", "scheduledDeparture", "scheduledArrival", "status", "hours", "published");

		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<Leg> legs) {

		int flightId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.flightRepository.findById(flightId);
		boolean isFlightPublished;
		isFlightPublished = flight.isPublished();
		super.getResponse().addGlobal("flightId", flightId);
		super.getResponse().addGlobal("isFlightPublished", isFlightPublished);
	}

}
