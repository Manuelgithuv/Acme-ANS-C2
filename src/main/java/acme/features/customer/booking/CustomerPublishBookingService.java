
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.TravelClass;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.features.manager.flight.FlightRepository;
import acme.realms.Customer;

@GuiService
public class CustomerPublishBookingService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository	bookingRepository;

	@Autowired
	private FlightRepository	flightRepository;


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

		Booking booking;

		int id;

		id = super.getRequest().getData("id", int.class);

		booking = this.bookingRepository.findById(id);

		super.getBuffer().addData(booking);

	}

	@Override
	public void bind(final Booking booking) {

		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardNibble");

	}

	@Override
	public void validate(final Booking booking) {
		if (booking.getLastCardNibble() != null) {
			super.state(false, "*", "customer.booking.create.null-lastCardNibble");
			return;
		}

		boolean status;
		if (booking.getFlight() == null) {
			super.state(false, "flight", "customer.booking.create.null-flight");
			return;
		}

		Optional<Booking> existingBooking = this.bookingRepository.findBookingsByLocatorCode(booking.getLocatorCode(), booking.getId());
		if (!existingBooking.isEmpty())
			super.state(false, "locatorCode", "customer.booking.locatorCode.alreadyExists");

		Optional<Booking> existingPassenger = this.bookingRepository.findBookingsByLocatorCode(booking.getLocatorCode(), booking.getId());
		if (!existingBooking.isEmpty())
			super.state(false, "locatorCode", "customer.booking.locatorCode.alreadyExists");

		Optional<Booking> allPassengerPublish = this.bookingRepository.findBookingsByLocatorCode(booking.getLocatorCode(), booking.getId());
		if (!existingBooking.isEmpty())
			super.state(false, "locatorCode", "customer.booking.locatorCode.alreadyExists");

		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		status = booking.getCustomer().getId() == customer.getId();

		super.state(status, "customer", "booking.customer.is.not.logged-customer");

	}

	@Override
	public void perform(final Booking booking) {

		booking.setPublished(true);
		this.bookingRepository.save(booking);

	}

	@Override
	public void unbind(final Booking booking) {

		Dataset dataset;
		Collection<Flight> flights = this.flightRepository.findPublicFlights();
		SelectChoices travelClassChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Flight flight = booking.getFlight() == null || booking.getFlight().getId() == 0 ? null : booking.getFlight();

		SelectChoices flightChoices = SelectChoices.from(flights, "tag", flight);

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardNibble", "published");

		dataset.put("travelClasses", travelClassChoices);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		super.getResponse().addData(dataset);
	}

}
