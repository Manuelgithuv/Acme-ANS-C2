
package acme.features.authenticated.assistanceAgent;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineRepository;
import acme.realms.AssistanceAgent;

@GuiService
public class AuthenticatedAssistanceAgentUpdateService extends AbstractGuiService<Authenticated, AssistanceAgent> {

	@Autowired
	private AuthenticatedAssistanceAgentRepository	repository;

	@Autowired
	private AirlineRepository						airlineRepository;


	@Override
	public void authorise() {
		boolean status;

		boolean airlineCheck = true;

		if (!super.getRequest().getMethod().equals("GET")) {

			int airlineCode = super.getRequest().getData("airlineCode", int.class);
			List<Airline> airlines = this.airlineRepository.findAllAirlines();
			if (airlines.stream().anyMatch(x -> x.getId() == airlineCode))
				airlineCheck = true;
			else
				airlineCheck = false;
		}

		status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class) && airlineCheck;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		AssistanceAgent agent;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		agent = this.repository.findAgentByUserId(userAccountId);

		super.getBuffer().addData(agent);

	}

	@Override
	public void bind(final AssistanceAgent agent) {
		if (!super.getRequest().getMethod().equals("GET")) {
			int airlineCode = super.getRequest().getData("airlineCode", int.class);
			Airline airline = this.airlineRepository.findById(airlineCode);
			agent.setAirline(airline);
		}
		super.bindObject(agent, "employeCode", "spokenLanguages", "dateStartingWorking", "bio", "salary", "picture");
	}

	@Override
	public void validate(final AssistanceAgent agent) {
		if (agent.getAirline() == null)
			super.state(false, "airlineCode", "airline cant be null");
	}

	@Override
	public void perform(final AssistanceAgent agent) {
		assert agent != null;
		this.repository.save(agent);

	}

	@Override
	public void unbind(final AssistanceAgent agent) {
		Dataset dataset;

		dataset = super.unbindObject(agent, "employeCode", "spokenLanguages", "dateStartingWorking", "bio", "salary", "picture");
		Collection<Airline> airlines = this.airlineRepository.findAllAirlines();
		SelectChoices airlineChoices = SelectChoices.from(airlines, "iataCode", agent.getAirline());
		dataset.put("airlineCode", airlineChoices.getSelected().getKey());
		dataset.put("airlineCodes", airlineChoices);
		super.getResponse().addData(dataset);

	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
