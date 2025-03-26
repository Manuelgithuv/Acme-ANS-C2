
package acme.features.costumer.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.leg.Leg;
import acme.features.manager.leg.LegRepository;
import acme.realms.Customer;

@GuiService
public class CustomerPublishBookingService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository	bookingRepository;

	@Autowired
	private LegRepository		legRepository;


	@Override
	public void authorise() {

		boolean status;

		Booking Booking;

		int id;

		id = super.getRequest().getData("id", int.class);

		Booking = this.bookingRepository.findById(id);

		boolean isBookingPublished = Booking != null && Booking.isPublished();

		status = !isBookingPublished;

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		Booking booking;

		int id;

		id = super.getRequest().getData("id", int.class);

		booking = this.bookingRepository.findById(id);

		super.getBuffer().addData(booking);

	}

	@Override
	public void bind(final Booking booking) {

		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardnibble");

	}

	@Override
	public void validate(final Booking booking) {

		boolean status;

		int id = booking.getId();

		List<Leg> legs = this.legRepository.findDistinctByBooking(id);
		boolean areLegsPublished = !legs.isEmpty() && legs.stream().allMatch(l -> l != null && l.isPublished());

		status = areLegsPublished;

		super.state(status, "*", "Customer.Booking.publish.not-all-legs-published");

	}

	@Override
	public void perform(final Booking booking) {

		booking.setPublished(true);
		this.bookingRepository.save(booking);

	}

	@Override
	public void unbind(final Booking booking) {

		Dataset dataset;

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardnibble");

		super.getResponse().addData(dataset);
	}

}
