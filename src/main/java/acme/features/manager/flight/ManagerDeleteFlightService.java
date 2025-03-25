
package acme.features.manager.flight;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.features.manager.leg.LegRepository;
import acme.realms.Manager;

@GuiService
public class ManagerDeleteFlightService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private FlightRepository	flightRepository;

	@Autowired
	private LegRepository		legRepository;


	@Override
	public void authorise() {

		boolean status;

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		int id = super.getRequest().getData("id", int.class);

		Flight flight = this.flightRepository.findById(id);

		boolean isFlightPublished = flight != null && flight.isPublished();

		boolean isFlightFromAuthenticatedManager = flight != null && flight.getManager().getId() == managerId;

		status = !isFlightPublished && isFlightFromAuthenticatedManager;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		Flight flight;

		int id;

		id = super.getRequest().getData("id", int.class);

		flight = this.flightRepository.findById(id);

		super.getBuffer().addData(flight);

	}

	@Override
	public void bind(final Flight flight) {

		super.bindObject(flight, "tag", "indication", "cost", "description");

	}

	@Override
	public void validate(final Flight flight) {
		boolean status;

		int id = flight.getId();

		List<Leg> legs = this.legRepository.findDistinctByFlight(id);
		boolean isSomeLegPublished = !legs.isEmpty() && legs.stream().anyMatch(l -> l != null && l.isPublished());

		status = !isSomeLegPublished;

		super.state(status, "*", "manager.flight.publish.there-is-at-least-one-leg-published");
	}

	@Override
	public void perform(final Flight flight) {

		List<Leg> legs = this.legRepository.findDistinctByFlight(flight.getId());
		legs.forEach(l -> this.legRepository.delete(l));

		this.flightRepository.delete(flight);

	}

	@Override
	public void unbind(final Flight flight) {

		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "indication", "cost", "published", "description", "scheduledDeparture", "scheduledArrival", "originCity", "destinationCity", "layovers");

		super.getResponse().addData(dataset);
	}

}
