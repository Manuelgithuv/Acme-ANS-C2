
package acme.features.authenticated.flight_crew;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.FlightCrew;

@GuiController
public class AuthenticatedFlightCrewController extends AbstractGuiController<Authenticated, FlightCrew> {

	@Autowired
	private AuthenticatedFlightCrewCreateService	create;
	@Autowired
	private AuthenticatedFlightCrewUpdateService	update;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.create);
		super.addBasicCommand("update", this.update);
	}
}
