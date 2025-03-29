
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
public class CustomerCreateBookingService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository	bookingRepository;

	@Autowired
	private FlightRepository	flightRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		Booking booking;
		Customer customer;

		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		booking = new Booking();
		booking.setPublished(false);
		booking.setCustomer(customer);
		super.getBuffer().addData(booking);

	}

	@Override
	public void bind(final Booking booking) {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.flightRepository.findById(flightId);

		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardNibble");

		booking.setFlight(flight);
	}

	@Override
	public void validate(final Booking booking) {
		boolean status;
		if (booking.getFlight() == null) {
			super.state(false, "flight", "customer.booking.create.null-flight");
			return;
		}

		Optional<Booking> existingBooking = this.bookingRepository.findBookingsByLocatorCode(booking.getLocatorCode(), booking.getId());
		if (!(existingBooking.isEmpty() || existingBooking.get().getId() != booking.getId()))
			super.state(false, "locatorCode", "customer.booking.locatorCode.alreadyExists");
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		status = booking.getCustomer().getId() == customer.getId();

		super.state(status, "customer", "booking.customer.is.not.logged-customer");
	}

	@Override
	public void perform(final Booking booking) {

		this.bookingRepository.save(booking);

	}

	@Override
	public void unbind(final Booking booking) {

		Dataset dataset;
		Collection<Flight> flights = this.flightRepository.findAllFlights();
		SelectChoices travelClassChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Flight flight = booking.getFlight() == null || booking.getFlight().getId() == 0 ? null : booking.getFlight();

		SelectChoices flightChoices = SelectChoices.from(flights, "tag", flight);

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "published");
		dataset.put("travelClasses", travelClassChoices);

		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		super.getResponse().addData(dataset);
	}
}
