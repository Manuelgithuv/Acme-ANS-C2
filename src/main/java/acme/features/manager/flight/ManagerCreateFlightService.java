
package acme.features.manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.MoneyService;
import acme.entities.flight.Flight;
import acme.realms.Manager;

@GuiService
public class ManagerCreateFlightService extends AbstractGuiService<Manager, Flight> {

	@Autowired
	private FlightRepository flightRepository;
	
	@Autowired MoneyService moneyService;


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

		super.bindObject(flight, "tag", "indication", "cost", "description");

	}

	@Override
	public void validate(final Flight flight) {
		boolean status;

		Manager manager = (Manager) super.getRequest().getPrincipal().getActiveRealm();

		status = flight.getManager().getId() == manager.getId();

		super.state(status, "manager", "flight.manager.is.not.logged-manager");
		
		boolean currencyState = flight.getCost() != null && this.moneyService.checkContains(flight.getCost().getCurrency());
		
		if(!currencyState) {
			super.state(currencyState, "cost", "manager.flight.invalid-currency");
		}
	}

	@Override
	public void perform(final Flight flight) {

		this.flightRepository.save(flight);

	}

	@Override
	public void unbind(final Flight flight) {

		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "indication", "cost", "published", "description", "scheduledDeparture", "scheduledArrival", "originCity", "destinationCity", "layovers");

		super.getResponse().addData(dataset);

	}

}
