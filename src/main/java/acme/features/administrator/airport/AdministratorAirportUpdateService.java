
package acme.features.administrator.airport;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.OperationalScope;
import acme.entities.airport.Airport;

@GuiService
public class AdministratorAirportUpdateService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AdministratorAirportRepository repository;


	@Override
	public void authorise() {
		
		int id = super.getRequest().hasData("id", int.class) ? super.getRequest().getData("id", int.class) : 0;
		Airport airport = this.repository.findAirportById(id);
		boolean status = airport !=null && id!=0;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airport airport;
		int id;

		id = super.getRequest().getData("id", int.class);
		airport = this.repository.findAirportById(id);

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {

		super.bindObject(airport, "name", "iataCode", "scope", "city", "country", "webSite", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airport airport) {
		
		Collection<Airport> airports = this.repository.findAllAirports();
		if(airport.getIataCode()!=null) {
			boolean iataRepeated = airports.stream().anyMatch(a->a.getIataCode().equals(airport.getIataCode()));
			super.state(!iataRepeated,"iataCode","acme.validation.repeatedAirportIataCode");
		}
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		SelectChoices types;
		Dataset dataset;

		types = SelectChoices.from(OperationalScope.class, airport.getScope());

		dataset = super.unbindObject(airport, "name", "iataCode", "scope", "city", "country", "webSite", "email", "phoneNumber");
		dataset.put("confirmation", false);
		dataset.put("types", types);

		super.getResponse().addData(dataset);
	}
}
