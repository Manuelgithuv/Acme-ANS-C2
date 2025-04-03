
package acme.features.administrator.passenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.passenger.Passenger;

@GuiController
public class AdministratorPassengerController extends AbstractGuiController<Administrator, Passenger> {

	@Autowired
	private AdministratorListPassengerByBookingService listPassengerByBookingService;


	@PostConstruct
	protected void initialise() {

		super.addCustomCommand("listPassengerByBooking", "list", this.listPassengerByBookingService);
	}

}
