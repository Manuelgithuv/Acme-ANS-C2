
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.AirportRepository;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.BookingRepository;
import acme.realms.Customer;

@GuiService
public class CustomerShowPassengerService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository	passengerRepository;

	@Autowired
	private AirportRepository	airportRepository;

	@Autowired
	private BookingRepository	bookingRepository;


	@Override
	public void authorise() {
		boolean status;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		int id = super.getRequest().getData("id", int.class);

		Passenger passenger = this.passengerRepository.findById(id);

		status = passenger.getCustomer().getId() == customerId;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		Passenger passenger;

		int id;

		id = super.getRequest().getData("id", int.class);

		passenger = this.passengerRepository.findById(id);

		super.getBuffer().addData(passenger);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset = this.buildDataset(passenger);

		super.getResponse().addData(dataset);
	}

	private Dataset buildDataset(final Passenger passenger) {
		return super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "published");
	}

}
