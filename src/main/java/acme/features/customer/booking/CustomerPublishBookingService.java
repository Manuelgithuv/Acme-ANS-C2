
package acme.features.customer.booking;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.components.MoneyService;
import acme.datatypes.TravelClass;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingPassenger;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.features.customer.bookingPassenger.BpRepository;
import acme.features.manager.flight.FlightRepository;
import acme.realms.Customer;

@GuiService
public class CustomerPublishBookingService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository	bookingRepository;

	@Autowired
	private BpRepository		bookingPassengerRepository;

	@Autowired
	private FlightRepository	flightRepository;

	@Autowired
	private MoneyService		moneyService;


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

		if (booking.getLastCardNibble() == null || booking.getLastCardNibble().trim().isEmpty()) {
			super.state(false, "*", "customer.booking.create.null-lastCardNibble");
			return;
		}
		boolean currencyState = booking.getPrice() != null && this.moneyService.checkContains(booking.getPrice().getCurrency());
		if (!currencyState)
			super.state(currencyState, "price", "manager.flight.invalid-currency");
		boolean status;
		boolean allPassengerPublish;
		boolean allBookingPassengerPublish;
		boolean atLeastOneBookingPassenger;
		if (booking.getFlight() == null) {
			super.state(false, "flight", "customer.booking.create.null-flight");
			return;
		}

		Optional<Booking> existingBooking = this.bookingRepository.findBookingsByLocatorCode(booking.getLocatorCode(), booking.getId());
		if (!existingBooking.isEmpty())
			super.state(false, "locatorCode", "customer.booking.locatorCode.alreadyExists");

		Collection<BookingPassenger> allBookingPassengerByBooking = this.bookingPassengerRepository.findBookingPassengersByBookingId(booking.getId());
		allBookingPassengerPublish = allBookingPassengerByBooking.stream().allMatch(p -> p.isPublished() == true);
		atLeastOneBookingPassenger = allBookingPassengerByBooking.size() > 0;
		if (!allBookingPassengerPublish)
			super.state(false, "*", "customer.booking.all-booking-passengers-are-published");
		if (!atLeastOneBookingPassenger)
			super.state(false, "*", "customer.booking.at-least-one-passenger");
		Collection<Passenger> allPassengerByBooking = this.bookingRepository.findPassengersByBookingId(booking.getId());
		allPassengerPublish = allPassengerByBooking.stream().allMatch(p -> p.isPublished() == true);
		if (!allPassengerPublish)
			super.state(false, "*", "customer.booking.all-passengers-are-published");

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
		SelectChoices flightChoices = new SelectChoices();
		Flight flight = booking.getFlight() == null || booking.getFlight().getId() == 0 ? null : booking.getFlight();

		flightChoices.add("0", "----", flight == null); // Opción vacía

		for (Flight f : flights) {
			String key = Integer.toString(f.getId());
			String label = f.getScheduledDeparture() + " - " + f.getScheduledArrival();
			boolean isSelected = f.equals(flight);

			flightChoices.add(key, label, isSelected);
		}

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCardNibble", "published");

		dataset.put("travelClasses", travelClassChoices);
		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		super.getResponse().addData(dataset);
	}

}
