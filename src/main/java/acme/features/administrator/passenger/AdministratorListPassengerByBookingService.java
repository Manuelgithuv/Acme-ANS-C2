
package acme.features.administrator.passenger;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.features.customer.booking.BookingRepository;

@GuiService
public class AdministratorListPassengerByBookingService extends AbstractGuiService<Administrator, Passenger> {

	@Autowired
	private AdministratorPassengerRepository	passengerRepository;

	@Autowired
	private BookingRepository					bookingRepository;


	@Override
	public void authorise() {
		boolean status;

		int bookingId = super.getRequest().getData("bookingId", int.class);

		Booking booking = this.bookingRepository.findById(bookingId);

		status = booking != null && booking.isPublished();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		int bookingId = super.getRequest().getData("bookingId", int.class);

		List<Passenger> passengers = this.passengerRepository.findPassengersByBookingId(bookingId);

		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Passenger passenger) {

		Dataset dataset;

		dataset = super.unbindObject(passenger, "fullName", "email", "passportNumber", "dateOfBirth", "specialNeeds", "published");

		super.getResponse().addData(dataset);

	}

	@Override
	public void unbind(final Collection<Passenger> passengers) {

		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.bookingRepository.findById(bookingId);
		boolean isBookingPublished;
		isBookingPublished = booking != null && booking.isPublished();
		super.getResponse().addGlobal("bookingId", bookingId);
		super.getResponse().addGlobal("isBookingPublished", isBookingPublished);
	}

}
