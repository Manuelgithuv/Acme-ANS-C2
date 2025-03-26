
package acme.features.manager.leg;

import java.util.Collection;

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
public class ManagerShowLegService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private LegRepository		legRepository;

	@Autowired
	private AirportRepository	airportRepository;

	@Autowired
	private AircraftRepository	aircraftRepository;


	@Override
	public void authorise() {
		boolean status;

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		int id = super.getRequest().getData("id", int.class);

		Leg leg = this.legRepository.findById(id);

		status = leg.getManager().getId() == managerId;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		Leg leg;

		int id;

		id = super.getRequest().getData("id", int.class);

		leg = this.legRepository.findById(id);

		super.getBuffer().addData(leg);

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset = this.buildDataset(leg);

		this.populateDatasetWithChoices(dataset, leg);

		super.getResponse().addData(dataset);
	}

	private Dataset buildDataset(final Leg leg) {
		return super.unbindObject(leg, "flightCode", "scheduledDeparture", "scheduledArrival", "status", "hours", "published");
	}

	private void populateDatasetWithChoices(final Dataset dataset, final Leg leg) {
		Collection<Airport> airports = this.airportRepository.findAllAirports();
		Collection<Aircraft> aircrafts = this.aircraftRepository.findAllAircrafts();

		SelectChoices departureAirportChoices = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		SelectChoices arrivalAirportChoices = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		if (departureAirportChoices.getSelected().getKey() != null)
			dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());
		dataset.put("departureAirports", departureAirportChoices);

		if (arrivalAirportChoices.getSelected().getKey() != null)
			dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalAirportChoices);

		if (aircraftChoices.getSelected().getKey() != null)
			dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
	}

}
