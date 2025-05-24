
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.AircraftRepository;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.BookingRepository;
import acme.realms.Customer;

@GuiService
public class CustomerUpdatePassengerService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository	passengerRepository;

	@Autowired
	private BookingRepository	bookingRepository;

	@Autowired
	private AircraftRepository	aircraftRepository;


	@Override
	public void authorise() {
		boolean status;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		int id = super.getRequest().hasData("id") ? super.getRequest().getData("id", int.class) : 0;

		Passenger passenger = this.passengerRepository.findById(id);

		status = passenger != null && passenger.getCustomer().getId() == customerId && !passenger.isPublished();

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
	public void bind(final Passenger passenger) {

		super.bindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds");
	}

	@Override
	public void validate(final Passenger passenger) {
		if (passenger == null) {
			super.state(false, "*", "Customer.Passenger.create.null-Passenger");
			return;
		}

		boolean status;

		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		if (passenger.getCustomer().getId() != customer.getId())
			super.state(false, "Customer", "Passenger.Customer.is.not.logged-Customer");
		;
	}

	@Override
	public void perform(final Passenger passenger) {
		this.passengerRepository.save(passenger);

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
