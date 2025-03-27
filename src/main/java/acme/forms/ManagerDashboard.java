
package acme.forms;


import acme.client.components.basis.AbstractForm;
import acme.entities.airport.Airport;
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

	private Airport					mostPopularAirport;

	private Airport					lessPopularAirport;

	private Integer 				numberOfLegsPending;
	
	private Integer 				numberOfLegsCancelled;
	
	private Integer 				numberOfLegsDelayed;
	
	private Integer 				numberOfLegsOnTTime;

	private Double					averageCostDesviationOfFlights;

	private Double					minimumCostDesviationOfFlights;

	private Double					maximumCostDesviationOfFlights;

	private Double					standardCostDesviationOfFlights;

}
