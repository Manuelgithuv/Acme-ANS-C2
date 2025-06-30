
package acme.features.assistanceAgents.dashboard;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.ClaimStatus;
import acme.entities.claim.Claim;
import acme.entities.claimLog.ClaimTrackingLog;
import acme.features.assistanceAgents.claim.AssistanceAgentClaimRepository;
import acme.features.assistanceAgents.trackingLog.AssistanceAgentTrackingLogRepository;
import acme.forms.AssistanceAgentDashboard;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentDashboardShowService extends AbstractGuiService<AssistanceAgent, AssistanceAgentDashboard> {

	@Autowired
	private AssistanceAgentClaimRepository			claimRepository;

	@Autowired
	private AssistanceAgentTrackingLogRepository	logRepository;


	@Override
	public void authorise() {
		Boolean status = super.getRequest().getPrincipal().hasRealmOfType(AssistanceAgent.class);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		List<Claim> claims = List.copyOf(this.claimRepository.findAllByAssistanceAgentId(agentId));

		AssistanceAgentDashboard dashboard = this.buildDashboard(claims, MomentHelper.getCurrentMoment());
		super.getBuffer().addData(dashboard);
	}

	public AssistanceAgentDashboard buildDashboard(final List<Claim> claims, final Date today) {
		AssistanceAgentDashboard dto = new AssistanceAgentDashboard();

		if (claims == null || claims.isEmpty()) {
			dto.setAverageClaimsLastMonth(0.0);
			dto.setMinimumClaimsLastMonth(0);
			dto.setMaximumClaimsLastMonth(0);
			dto.setStandardDeviationClaimsLastMonth(0.0);

			dto.setResolvedClaimsRatio(0.0);
			dto.setRejectedClaimsRatio(0.0);

			dto.setAverageLogsPerClaim(0.0);
			dto.setMinimumLogsPerClaim(0);
			dto.setMaximumLogsPerClaim(0);
			dto.setStandardDeviationLogsPerClaim(0.0);

			dto.setTopThreeMonthsWithMostClaims(List.of("None", "None", "None"));

			return dto;
		}

		//The ratio of claims that have been resolved successfully.
		double claimListSize = claims.size();
		double resolvedClaims = claims.stream().filter(z -> z.getStatus() == ClaimStatus.ACCEPTED).count();
		dto.setResolvedClaimsRatio(resolvedClaims / claimListSize);
		// Ratio of claims that have been rejected
		double rejectedClaims = claims.stream().filter(z -> z.getStatus() == ClaimStatus.REJECTED).count();
		dto.setRejectedClaimsRatio(rejectedClaims / claimListSize);
		// The top three months with the highest number of claims
		Map<Integer, Integer> monthCounts = new HashMap<>();

		for (int i = 0; i < 12; i++)
			monthCounts.put(i, 0);

		Calendar calendar = Calendar.getInstance();

		// Contar apariciones de cada mes
		for (Claim c : claims) {
			Date date = c.getRegistrationMoment();
			calendar.setTime(date);
			int month = calendar.get(Calendar.MONTH);
			monthCounts.put(month, monthCounts.get(month) + 1);
		}

		List<Map.Entry<Integer, Integer>> sortedEntries = new ArrayList<>(monthCounts.entrySet());
		sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

		// Obtener nombres de los meses
		String[] monthNames = new DateFormatSymbols().getMonths(); // Enero a Diciembre en inglés (o en español si locale)

		List<String> topThree = new ArrayList<>();

		for (int i = 0; i < 3 && i < sortedEntries.size(); i++) {
			int monthIndex = sortedEntries.get(i).getKey();
			topThree.add(monthNames[monthIndex]);
		}

		dto.setTopThreeMonthsWithMostClaims(topThree);

		// Statistics for the number of logs their claims have
		List<Integer> logsCount = new ArrayList<>();
		for (Claim c : claims) {
			List<ClaimTrackingLog> ls = this.logRepository.findAllByClaimIdOrderByCreationMomentDescIdDesc(c.getId());
			logsCount.add(ls.size());
		}

		Map<String, Double> logsCountStadistics = this.calculateStatistics(logsCount);
		dto.setAverageLogsPerClaim(logsCountStadistics.get("average"));
		dto.setMinimumLogsPerClaim((int) Math.round(logsCountStadistics.get("min")));
		dto.setMaximumLogsPerClaim((int) Math.round(logsCountStadistics.get("max")));
		dto.setStandardDeviationLogsPerClaim(logsCountStadistics.get("standardDeviation"));

		List<Integer> lastMonthCountPerAgent = new ArrayList<>();
		List<AssistanceAgent> agents = List.copyOf(this.claimRepository.findAllAssistanceAgents());
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(today);
		calendar2.add(Calendar.MONTH, -1); // Restar un mes
		Date lastMonth = calendar2.getTime();
		for (AssistanceAgent a : agents) {
			List<Claim> claimsOfAgent = List.copyOf(this.claimRepository.findAllByAssistanceAgentId(a.getId()));
			int count = (int) claimsOfAgent.stream().filter(z -> z.getRegistrationMoment().after(lastMonth)).count();
			lastMonthCountPerAgent.add(count);
		}

		Map<String, Double> lastMonthStadistics = this.calculateStatistics(lastMonthCountPerAgent);

		dto.setAverageClaimsLastMonth(lastMonthStadistics.get("average"));
		dto.setMinimumClaimsLastMonth((int) Math.round(lastMonthStadistics.get("min")));
		dto.setMaximumClaimsLastMonth((int) Math.round(lastMonthStadistics.get("max")));
		dto.setStandardDeviationClaimsLastMonth(lastMonthStadistics.get("standardDeviation"));
		return dto;
	}

	public Map<String, Double> calculateStatistics(final List<Integer> numbers) {
		Map<String, Double> stats = new HashMap<>();

		if (numbers == null || numbers.isEmpty()) {
			stats.put("max", 0.0);
			stats.put("min", 0.0);
			stats.put("average", 0.0);
			stats.put("standardDeviation", 0.0);
			return stats;
		}

		int sum = 0;
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;

		for (int num : numbers) {
			sum += num;
			if (num > max)
				max = num;
			if (num < min)
				min = num;
		}

		double average = sum / (double) numbers.size();

		// Calcular desviación estándar
		double varianceSum = 0.0;
		for (int num : numbers)
			varianceSum += Math.pow(num - average, 2);
		double standardDeviation = Math.sqrt(varianceSum / numbers.size());

		stats.put("max", (double) max);
		stats.put("min", (double) min);
		stats.put("average", average);
		stats.put("standardDeviation", standardDeviation);

		return stats;
	}

	@Override
	public void unbind(final AssistanceAgentDashboard dashboard) {
		Dataset dataset = super.unbindObject(dashboard, "resolvedClaimsRatio", "rejectedClaimsRatio", "averageLogsPerClaim", "minimumLogsPerClaim", "maximumLogsPerClaim", "standardDeviationLogsPerClaim", "averageClaimsLastMonth", "minimumClaimsLastMonth",
			"maximumClaimsLastMonth", "standardDeviationClaimsLastMonth");

		String resultado = String.join(", ", dashboard.getTopThreeMonthsWithMostClaims());
		dataset.put("topThreeMonthsWithMostClaims", resultado);

		super.getResponse().addData(dataset);

	}
}
