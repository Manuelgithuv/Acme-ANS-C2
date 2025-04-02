
package acme.features.customer.bookingPassenger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.booking.BookingPassenger;
import acme.realms.Customer;

@GuiController
public class CustomerBookingPassengerController extends AbstractGuiController<Customer, BookingPassenger> {

	@Autowired
	private CustomerListBookingPassengerService		listService;

	@Autowired
	private CustomerShowBookingPassengerService		showService;

	@Autowired
	private CustomerCreateBookingPassengerService	createService;

	@Autowired
	private CustomerUpdateBookingPassengerService	updateService;

	@Autowired
	private CustomerPublishBookingPassengerService	publishService;
	@Autowired
	private CustomerDeleteBookingPassengerService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
