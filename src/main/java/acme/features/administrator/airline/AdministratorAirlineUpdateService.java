
package acme.features.administrator.airline;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AirlineType;
import acme.entities.airline.Airline;

@GuiService
public class AdministratorAirlineUpdateService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AdministratorAirlineRepository repository;


	@Override
	public void authorise() {
		int id = super.getRequest().hasData("id", int.class) ? super.getRequest().getData("id", int.class) : 0;
		Airline airline = this.repository.findAirlineById(id);
		boolean status = airline !=null && id!=0;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airline airline;
		int id;

		id = super.getRequest().getData("id", int.class);
		airline = this.repository.findAirlineById(id);

		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {

		super.bindObject(airline, "name", "iataCode", "webSite", "type", "foundationMoment", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airline airline) {

		Collection<Airline> airlines = this.repository.findAllAirlines();
		if(airline.getIataCode()!=null) {
			boolean iataRepeated = airlines.stream().anyMatch(a->a.getIataCode().equals(airline.getIataCode()));
			super.state(!iataRepeated,"iataCode","acme.validation.repeatedAirlineIataCode");
		}
		boolean confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		SelectChoices types;
		Dataset dataset;

		types = SelectChoices.from(AirlineType.class, airline.getType());

		dataset = super.unbindObject(airline, "name", "iataCode", "webSite", "type", "foundationMoment", "email", "phoneNumber");
		dataset.put("confirmation", false);
		dataset.put("types", types);

		super.getResponse().addData(dataset);
	}
}
