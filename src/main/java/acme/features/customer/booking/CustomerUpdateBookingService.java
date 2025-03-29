
package acme.features.costumer.booking;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.realms.Customer;

@GuiService
public class CustomerUpdateBookingService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository bookingRepository;


	@Override
	public void authorise() {

		boolean status;

		Booking booking;

		int id;

		id = super.getRequest().getData("id", int.class);

		booking = this.bookingRepository.findById(id);

		boolean isBookingPublished = booking != null && booking.isPublished();

		status = !isBookingPublished;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		Booking Booking;

		int id;

		id = super.getRequest().getData("id", int.class);

		Booking = this.bookingRepository.findById(id);

		super.getBuffer().addData(Booking);

	}

	@Override
	public void bind(final Booking booking) {

		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardnibble");

	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {

		this.bookingRepository.save(booking);

	}

	@Override
	public void unbind(final Booking booking) {

		Dataset dataset;

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardnibble");

		super.getResponse().addData(dataset);
	}

}
