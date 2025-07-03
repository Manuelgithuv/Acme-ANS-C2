
package acme.features.authenticated.flight_crew;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.Availability;
import acme.entities.airline.Airline;
import acme.features.administrator.airline.AdministratorAirlineRepository;
import acme.realms.FlightCrew;

@GuiService
public class AuthenticatedFlightCrewUpdateService extends AbstractGuiService<Authenticated, FlightCrew> {

	@Autowired
	private AuthenticatedFlightCrewRepository	authenticatedFlightCrewRepository;
	@Autowired
	private AdministratorAirlineRepository		airlineRepository;


	@Override
	public void authorise() {
		boolean status;
		status = super.getRequest().getPrincipal().hasRealmOfType(FlightCrew.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightCrew flightCrew;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		flightCrew = this.authenticatedFlightCrewRepository.findOneFlightCrewByUserAccountId(userAccountId);

		super.getBuffer().addData(flightCrew);
	}

	@Override
	public void bind(final FlightCrew flightCrew) {
		assert flightCrew != null;
		super.bindObject(flightCrew, "identifier", "phone", "languageSkills", "experience", "salary");
	}

	@Override
	public void validate(final FlightCrew flightCrew) {
		assert flightCrew != null;

		Optional<FlightCrew> existingFlightCrew = this.authenticatedFlightCrewRepository.findByIdentifierNumber(flightCrew.getIdentifier());

		if (!existingFlightCrew.isEmpty())
			super.state(false, "identifier", "flightCrew.authenticated.invalid");
	}

	@Override
	public void perform(final FlightCrew flightCrew) {
		assert flightCrew != null;
		this.authenticatedFlightCrewRepository.save(flightCrew);
	}

	@Override
	public void unbind(final FlightCrew flightCrew) {
		Dataset dataset;
		dataset = super.unbindObject(flightCrew, "identifier", "phone", "languageSkills", "experience", "salary");
		super.getResponse().addData(dataset);

		SelectChoices availabilityChoices = SelectChoices.from(Availability.class, flightCrew.getAvailability());
		dataset.put("availability", availabilityChoices.getSelected().getKey());
		dataset.put("availabilities", availabilityChoices);

		Collection<Airline> airlines = this.airlineRepository.findAllAirlines();
		SelectChoices airlineChoices = SelectChoices.from(airlines, "iataCode", flightCrew.getAirline());
		dataset.put("airline", airlineChoices.getSelected().getKey());
		dataset.put("airlines", airlineChoices);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
