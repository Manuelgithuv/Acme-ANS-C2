
package acme.forms;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	private Integer				rankingPosition;

	private Integer				yearsToRetire;

	private Double				ratioOnTimeLegs;

	private Double				ratioDelayedLegs;

	private Airport				mostPopularAirport;

	private Airport				leastPopularAirport;

	private int					numberOfLegsLanded;

	private int					numberOfLegsCancelled;

	private int					numberOfLegsDelayed;

	private int					numberOfLegsOnTime;

	private Money				avgDesviationOfCost;

	private Money				minDesviationOfCost;

	private Money				maxDesviationOfCost;

	private Money				standardDesviationOfCost;

}
