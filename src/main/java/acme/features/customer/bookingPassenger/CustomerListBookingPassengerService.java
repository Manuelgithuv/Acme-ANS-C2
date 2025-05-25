
package acme.features.customer.bookingPassenger;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
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
	private BpRepository		bookingPassengerRepository;

	@Autowired
	private BookingRepository	bookingRepository;

	@Autowired
	private PassengerRepository	passengerRepository;


	@Override
	public void authorise() {
		boolean status;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		List<BookingPassenger> bookingPassengers = this.bookingPassengerRepository.findBookingPassengersByCustomerId(customerId);

		status = bookingPassengers.stream().allMatch(bp -> bp.getCustomer().getId() == customerId || bp.isPublished());

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

		Collection<Passenger> passengers = this.passengerRepository.findAvailablePassengers(customer.getId());
		Passenger passenger = bookingPassenger.getPassenger() == null || bookingPassenger.getPassenger().getId() == 0 ? null : bookingPassenger.getPassenger();

		dataset = super.unbindObject(bookingPassenger, "published");

		String fullName = passenger.getFullName();
		String shortName = fullName.length() > 15 ? fullName.substring(0, 15) : fullName;

		dataset.put("booking", booking.getLocatorCode());

		dataset.put("passenger", shortName);

		super.getResponse().addData(dataset);

	}

}
