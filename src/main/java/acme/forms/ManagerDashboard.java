
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.LegStatus;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	private static final long		serialVersionUID	= 1L;

	private Integer					rankingPosition;

	private Integer					yearsToRetire;

	private Double					ratioOnTimeLegs;

	private Double					ratioDelayedLegs;

	private Map<Airport, Flight>	mostPopularAirports;

	private Map<Airport, Flight>	lessPopularAirports;

	private Map<Leg, LegStatus>		legsByStatus;

	private Double					averageCostDesviationOfFlights;

	private Double					minimumCostDesviationOfFlights;

	private Double					maximumCostDesviationOfFlights;

	private Double					standardCostDesviationOfFlights;

}
