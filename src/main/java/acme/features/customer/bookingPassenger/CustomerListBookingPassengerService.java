
package acme.features.customer.bookingPassenger;

import java.util.Collection;
import java.util.List;

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
public class CustomerListBookingPassengerService extends AbstractGuiService<Customer, BookingPassenger> {

	@Autowired
	private BookingPassengerRepository	bookingPassengerRepository;

	@Autowired
	private BookingRepository			bookingRepository;

	@Autowired
	private PassengerRepository			passengerRepository;


	@Override
	public void authorise() {
		boolean status;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		List<BookingPassenger> bookingPassengers = this.bookingPassengerRepository.findBookingPassengersByCustomerId(customerId);

		status = bookingPassengers.stream().allMatch(bp -> bp.getCustomer().getId() == customerId);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		List<BookingPassenger> bookingPassengers = this.bookingPassengerRepository.findBookingPassengersByCustomerId(customerId);

		super.getBuffer().addData(bookingPassengers);
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
