
package acme.features.authenticated.flight_crew;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.MoneyService;
import acme.datatypes.Availability;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineRepository;
import acme.realms.FlightCrew;

@GuiService
public class AuthenticatedFlightCrewCreateService extends AbstractGuiService<Authenticated, FlightCrew> {

	@Autowired
	private AuthenticatedFlightCrewRepository	repository;
	@Autowired
	private MoneyService						moneyService;
	@Autowired
	private AirlineRepository					airlineRepository;


	public AuthenticatedFlightCrewCreateService() {
	}

	@Override
	public void authorise() {
		boolean status = !super.getRequest().getPrincipal().hasRealmOfType(FlightCrew.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int userAccountId = super.getRequest().getPrincipal().getAccountId();
		UserAccount userAccount = this.repository.findOneUserAccountById(userAccountId);
		FlightCrew crew = new FlightCrew();
		crew.setUserAccount(userAccount);
		super.getBuffer().addData(crew);
	}

	@Override
	public void bind(final FlightCrew crew) {
		super.bindObject(crew, new String[] {
			"identifier", "phone", "language-skills", "availability", "salary", "experience"
		});
		String airlineCode = super.getRequest().getData("airline", String.class);
		Airline airline = this.airlineRepository.findByIATACode(airlineCode);
		crew.setAirline(airline);
	}

	@Override
	public void validate(final FlightCrew crew) {
		assert crew != null;

		Optional<FlightCrew> existing = this.repository.findByIdentifier(crew.getIdentifier());
		if (!this.moneyService.checkContains(crew.getSalary().getCurrency()))
			super.state(false, "salary", "authenticated.flight-crew.invalid-salary-currency", new Object[0]);

		if (!existing.isEmpty())
			super.state(false, "identifier", "flight-crew.authenticated.invalid-identifier", new Object[0]);

	}

	@Override
	public void perform(final FlightCrew crew) {
		assert crew != null;

		this.repository.save(crew);
	}

	@Override
	public void unbind(final FlightCrew crew) {
		SelectChoices availabilityChoices = SelectChoices.from(Availability.class, crew.getAvailability());
		Collection<Airline> airlines = this.airlineRepository.findAllAirlines();
		SelectChoices airlineChoices = SelectChoices.from(airlines, "iataCode", crew.getAirline());
		Dataset dataset = super.unbindObject(crew, new String[] {
			"identifier", "phone", "language-skills", "availability", "salary", "experience"
		});
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		dataset.put("availability", availabilityChoices);
		dataset.put("airline", airlineChoices);
		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();

	}
}
