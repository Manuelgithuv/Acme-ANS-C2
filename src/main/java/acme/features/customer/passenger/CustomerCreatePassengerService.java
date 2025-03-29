
package acme.features.costumer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.AircraftRepository;
import acme.entities.airport.AirportRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingPassenger;
import acme.entities.passenger.Passenger;
import acme.features.costumer.booking.BookingRepository;
import acme.realms.Customer;

@GuiService
public class CustomerCreatePassengerService extends AbstractGuiService<Customer, Passenger> {

	@Autowired
	private PassengerRepository	passengerRepository;

	@Autowired
	private AirportRepository	airportRepository;

	@Autowired
	private BookingRepository	bookingRepository;

	@Autowired
	private AircraftRepository	aircraftRepository;


	@Override
	public void authorise() {

		boolean status;

		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.bookingRepository.findById(bookingId);

		Customer Customer;

		Customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		status = !booking.isPublished() && Customer.getId() == booking.getCustomer().getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Passenger passenger;
		Customer customer;
		BookingPassenger bookingPassenger;

		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.bookingRepository.findById(bookingId);
		passenger = new Passenger();
		passenger.setPublished(false);
		bookingPassenger = new BookingPassenger();
		bookingPassenger.setBooking(booking);
		bookingPassenger.setPassenger(passenger);
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
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.bookingRepository.findById(bookingId);

		if (booking.getCustomer().getId() != customer.getId())
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
		dataset.put("bookingId", super.getRequest().getData("bookingId", int.class));

		super.getResponse().addData(dataset);
	}

	private Dataset buildDataset(final Passenger Passenger) {
		return super.unbindObject(Passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "published");
	}

}
