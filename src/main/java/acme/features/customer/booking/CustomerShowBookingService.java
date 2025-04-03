
package acme.features.customer.booking;

import java.util.Collection;

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
public class CustomerShowBookingService extends AbstractGuiService<Customer, Booking> {

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

		int id;

		id = super.getRequest().getData("id", int.class);

		booking = this.bookingRepository.findById(id);

		super.getBuffer().addData(booking);

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

		dataset.put("travelClas", travelClassChoices.getSelected());
		dataset.put("travelClasses", travelClassChoices);

		dataset.put("flight", flightChoices.getSelected().getKey());
		dataset.put("flights", flightChoices);
		super.getResponse().addData(dataset);

	}

}
