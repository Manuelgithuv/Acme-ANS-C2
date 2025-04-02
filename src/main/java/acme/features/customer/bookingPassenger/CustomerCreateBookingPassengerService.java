
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
public class CustomerCreateBookingPassengerService extends AbstractGuiService<Customer, BookingPassenger> {

	@Autowired
	private BookingPassengerRepository	bookingPassengerRepository;

	@Autowired
	private BookingRepository			bookingRepository;

	@Autowired
	private PassengerRepository			passengerRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		BookingPassenger bookingPassenger;
		Customer customer;

		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		bookingPassenger = new BookingPassenger();
		bookingPassenger.setPublished(false);
		bookingPassenger.setCustomer(customer);
		super.getBuffer().addData(bookingPassenger);

	}

	@Override
	public void bind(final BookingPassenger bookingPassenger) {

		super.bindObject(bookingPassenger);

	}

	@Override
	public void validate(final BookingPassenger bookingPassenger) {
		if (bookingPassenger.getBooking() != null) {
			super.state(false, "booking", "customer.bookingPassenger.create.null-booking");
			return;
		}

		if (bookingPassenger.getPassenger() == null) {
			super.state(false, "passenger", "customer.booking.create.null-passenger");
			return;
		}
	}

	@Override
	public void perform(final BookingPassenger bookingPassenger) {

		this.bookingPassengerRepository.save(bookingPassenger);

	}

	@Override
	public void unbind(final BookingPassenger bookingPassenger) {

		Dataset dataset;
		Customer customer;
		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();
		Collection<Booking> bookings = this.bookingRepository.findBookingsNotPublishedByCustomerId(customer.getId());
		Booking booking = bookingPassenger.getBooking() == null || bookingPassenger.getBooking().getId() == 0 ? null : bookingPassenger.getBooking();

		SelectChoices bookingChoices = SelectChoices.from(bookings, "locatorCode", booking);

		Collection<Passenger> passengers = this.passengerRepository.findAvailablePassengers(customer.getId());
		Passenger passenger = bookingPassenger.getPassenger() == null || bookingPassenger.getPassenger().getId() == 0 ? null : bookingPassenger.getPassenger();

		SelectChoices passengerChoices = SelectChoices.from(passengers, "fullName", passenger);

		dataset = super.unbindObject(bookingPassenger, "published");

		dataset.put("booking", bookingChoices.getSelected().getKey());
		dataset.put("bookings", bookingChoices);
		dataset.put("passenger", passengerChoices.getSelected().getKey());
		dataset.put("passengers", passengerChoices);
		super.getResponse().addData(dataset);
	}
}
