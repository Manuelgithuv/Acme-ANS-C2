
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdministratorDashboard extends AbstractForm {

	/*
	 * The system must handle administrator dashboards with the following indicators:
	 * • Total number of airports grouped by their operational scope.
	 * • Number of airlines grouped by their type.
	 * • Ratio of airlines with both an email address and a phone number.
	 * • Ratios of active and non-active aircrafts.
	 * • Ratio of reviews with a score above 5.00.
	 * • Count, average, minimum, maximum, and standard deviation of the number of reviews posted over the last 10 weeks.
	 */

	private static final long		serialVersionUID	= 1L;

	// Total number of airports grouped by their operational scope
	private Map<String, Integer>	airportsByScope;

	// Number of airlines grouped by their type
	private Map<String, Integer>	airlinesByType;

	// Ratio of airlines with both an email address and a phone number
	private Double					airlinesWithContactRatio;

	// Ratios of active and non-active aircrafts
	private Double					activeAircraftRatio;
	private Double					nonActiveAircraftRatio;

	// Ratio of reviews with a score above 5.00
	private Double					highScoreReviewRatio;

	// Count, average, minimum, maximum, and standard deviation of the number of reviews posted over the last 10 weeks
	private Integer					reviewCount;
	private Double					averageReviews;
	private Double					minimumReviews;
	private Double					maximumReviews;
	private Double					standardDeviationReviews;
}
