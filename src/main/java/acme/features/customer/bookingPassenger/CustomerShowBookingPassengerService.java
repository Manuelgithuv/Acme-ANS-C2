
package acme.features.customer.bookingPassenger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingPassenger;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.BookingRepository;
import acme.features.customer.passenger.PassengerRepository;
import acme.realms.Customer;

@GuiService
public class CustomerShowBookingPassengerService extends AbstractGuiService<Customer, BookingPassenger> {

	@Autowired
	private BpRepository		bookingPassengerRepository;

	@Autowired
	private BookingRepository	bookingRepository;

	@Autowired
	private PassengerRepository	passengerRepository;


	@Override
	public void authorise() {
		boolean status;

		BookingPassenger bookingPassenger;

		int id = super.getRequest().hasData("id") ? super.getRequest().getData("id", int.class) : 0;

		bookingPassenger = this.bookingPassengerRepository.findById(id);

		Customer customer;

		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		status = bookingPassenger != null && (bookingPassenger.isPublished() || !bookingPassenger.isPublished() && customer.getId() == bookingPassenger.getCustomer().getId());

		super.getResponse().setAuthorised(status);

	}
	@Override
	public void bind(final BookingPassenger bookingPassenger) {
		int bookingId;
		Booking booking;

		int passengerId;
		Passenger passenger;

		bookingId = super.getRequest().getData("booking", int.class);
		booking = this.bookingRepository.findById(bookingId);

		passengerId = super.getRequest().getData("passenger", int.class);
		passenger = this.passengerRepository.findById(passengerId);

		super.bindObject(bookingPassenger);

		bookingPassenger.setBooking(booking);

		bookingPassenger.setPassenger(passenger);

	}

	@Override
	public void load() {

		BookingPassenger bookingPassenger;

		int id;

		id = super.getRequest().getData("id", int.class);

		bookingPassenger = this.bookingPassengerRepository.findById(id);

		super.getBuffer().addData(bookingPassenger);

	}

	@Override
	public void unbind(final BookingPassenger bookingPassenger) {
		Dataset dataset;
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		Collection<Booking> bookings = this.bookingRepository.findBookingsNotPublishedByCustomerId(customer.getId());
		if (!bookings.contains(bookingPassenger.getBooking()))
			bookings.add(bookingPassenger.getBooking());

		Collection<Passenger> passengers = this.passengerRepository.findAvailablePassengers(customer.getId());
		if (!passengers.contains(bookingPassenger.getPassenger()))
			passengers.add(bookingPassenger.getPassenger());

		dataset = super.unbindObject(bookingPassenger, "published");

		SelectChoices bookingChoices = SelectChoices.from(bookings, "locatorCode", bookingPassenger.getBooking());
		SelectChoices passengerChoices = SelectChoices.from(passengers, "fullName", bookingPassenger.getPassenger());

		dataset.put("bookings", bookingChoices);
		dataset.put("passengers", passengerChoices);

		super.getResponse().addData(dataset);
	}

}
