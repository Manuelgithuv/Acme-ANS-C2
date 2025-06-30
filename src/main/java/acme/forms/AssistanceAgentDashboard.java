
package acme.forms;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

	/*
	 * The system must handle assistance agent dashboards with the following indicators:
	 * • The ratio of claims that have been resolved successfully.
	 * • The ratio of claims that have been rejected.
	 * • The top three months with the highest number of claims.
	 * • The average, minimum, maximum, and standard deviation of the number of logs their claims have.
	 * • The average, minimum, maximum, and standard deviation of the number of claims they assisted during the last month.
	 * 
	 */

	private static final long	serialVersionUID	= 1L;

	// Ratio of claims that have been resolved successfully
	private Double				resolvedClaimsRatio;

	// Ratio of claims that have been rejected
	private Double				rejectedClaimsRatio;

	// The top three months with the highest number of claims
	private List<String>		topThreeMonthsWithMostClaims;

	// Statistics for the number of logs their claims have
	private Double				averageLogsPerClaim;
	private Integer				minimumLogsPerClaim;
	private Integer				maximumLogsPerClaim;
	private Double				standardDeviationLogsPerClaim;

	// Statistics for the number of claims they assisted during the last month
	private Double				averageClaimsLastMonth;
	private Integer				minimumClaimsLastMonth;
	private Integer				maximumClaimsLastMonth;
	private Double				standardDeviationClaimsLastMonth;
}
