
package acme.features.customer.booking;

import java.util.Collection;
import java.util.List;

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
public class CustomerListBookingService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private BookingRepository	bookingRepository;

	@Autowired
	private FlightRepository	flightRepository;


	@Override
	public void authorise() {
		boolean status;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		List<Booking> bookings = this.bookingRepository.findBookingsByCustomerId(customerId);

		status = bookings.stream().allMatch(b -> b.getCustomer().getId() == customerId);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		List<Booking> bookings = this.bookingRepository.findBookingsByCustomerId(customerId);

		super.getBuffer().addData(bookings);
	}

	@Override
	public void unbind(final Booking booking) {

		Dataset dataset;
		Collection<Flight> flights = this.flightRepository.findAllFlights();
		SelectChoices travelClassChoices = SelectChoices.from(TravelClass.class, booking.getTravelClass());
		Flight flight = booking.getFlight() == null || booking.getFlight().getId() == 0 ? null : booking.getFlight();

		SelectChoices flightChoices = new SelectChoices();

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
