
package acme.features.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.OperationalScope;
import acme.entities.airport.Airport;

@GuiService
public class AdministratorAirportShowService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AdministratorAirportRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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
	public void unbind(final Airport airport) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(OperationalScope.class, airport.getScope());

		dataset = super.unbindObject(airport, "name", "iataCode", "webSite", "scope", "country", "city", "email", "phoneNumber");
		dataset.put("confirmation", true);
		dataset.put("readonly", false);
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

}
