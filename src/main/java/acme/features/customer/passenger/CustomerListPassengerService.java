
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.BookingRepository;
import acme.realms.Customer;

@GuiService
public class CustomerListPassengerService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository	passengerRepository;

	@Autowired
	private BookingRepository	bookingRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		List<Passenger> passengers = this.passengerRepository.findPassengersByCustomerId(customerId);

		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Passenger passenger) {

		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "published");

		super.getResponse().addData(dataset);

	}

}
