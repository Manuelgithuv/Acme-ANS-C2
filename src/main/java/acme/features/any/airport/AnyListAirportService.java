
package acme.features.any.airport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.airport.AirportRepository;

@GuiService
public class AnyListAirportService extends AbstractGuiService<Any, Airport> {

	@Autowired
	private AirportRepository airportRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Airport> airports = (List<Airport>) this.airportRepository.findAllAirports();

		super.getBuffer().addData(airports);

	}

	@Override
	public void unbind(final Airport airport) {

		Dataset dataset;

		dataset = super.unbindObject(airport, "name", "iataCode", "scope", "city", "country", "webSite", "email", "phoneNumber");

		super.getResponse().addData(dataset);

	}

}
