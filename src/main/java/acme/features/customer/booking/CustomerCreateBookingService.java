
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
import acme.entities.flight.Flight;
import acme.features.manager.flight.FlightRepository;
import acme.realms.Customer;

@GuiService
public class CustomerCreateBookingService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository	bookingRepository;

	@Autowired
	private FlightRepository	flightRepository;

	@Autowired
	private MoneyService		moneyService;


	@Override
	public void authorise() {

		boolean entitiesExist = true;

		if (!super.getRequest().getMethod().equals("GET")) {

			int flightId = super.getRequest().getData("flight", int.class);

			if (flightId != 0 && this.flightRepository.findById(flightId) == null)
				entitiesExist = false;
		}

		super.getResponse().setAuthorised(entitiesExist);
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
		if (!booking.getFlight().isPublished()) {
			super.state(false, "flight", "customer.booking.create.flight-is-not-published");
			return;
		}
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		boolean currencyState = booking.getPrice() != null && this.moneyService.checkContains(booking.getPrice().getCurrency());
		if (!currencyState)
			super.state(currencyState, "price", "manager.flight.invalid-currency");

		status = booking.getCustomer() != null && booking.getCustomer().getId() == customer.getId();

		Optional<Booking> existingBooking = this.bookingRepository.findBookingsByLocatorCode(booking.getLocatorCode(), booking.getId());
		if (!existingBooking.isEmpty())
			super.state(false, "locatorCode", "customer.booking.locatorCode.alreadyExists");

		super.state(status, "customer", "booking.customer.is.not.logged-customer");
	}

	@Override
	public void perform(final Booking booking) {

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

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "published");
		dataset.put("travelClasses", travelClassChoices);

		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		super.getResponse().addData(dataset);
	}
}
