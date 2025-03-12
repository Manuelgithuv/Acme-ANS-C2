
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.TravelClass;
import acme.entities.booking.Booking;

public class CustomerDashboard extends AbstractForm {

	private static final long			serialVersionUID	= 1L;

	private List<Booking>				lastFiveDestinations;

	private Double						moneySpentLastYear;

	private Map<Booking, TravelClass>	bookingsByTravelClass;

	private Integer						countBookingLastFiveYears;

	private Double						averageBookingLastFiveYears;

	private Double						minimumBookingLastFiveYears;

	private Double						maximumBookingLastFiveYears;

	private Double						standardDeviationBookingLastFiveYears;

	private Integer						countPassengers;

	private Double						averagePassengers;

	private Double						minimumPassengers;

	private Double						maximumPassengers;

	private Double						standardDeviationPassengers;
}
