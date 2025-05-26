
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
public class AdministratorAirlineCreateService extends AbstractGuiService<Administrator, Airline> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airline airline;

		airline = new Airline();

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
		dataset.put("confirmation", true);
		dataset.put("readonly", false);
		dataset.put("types", types);

		super.getResponse().addData(dataset);
	}

}
