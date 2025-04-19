
package acme.features.manager.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.datatypes.Money;
import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.airport.AirportRepository;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.system_configuration.SystemConfiguration;
import acme.forms.ManagerDashboard;
import acme.realms.Manager;

@GuiService
public class ManagerDashboardShowService extends AbstractGuiService<Manager, ManagerDashboard> {

	@Autowired
	private ManagerDashboardRepository	dashboardRepository;

	@Autowired
	private AirportRepository			airportRepository;


	@Override
	public void authorise() {
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Manager manager = this.dashboardRepository.findManagerById(managerId);
		boolean status = manager != null && super.getRequest().getPrincipal().hasRealm(manager);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		ManagerDashboard dashboard = new ManagerDashboard();

		Integer rankingPosition;

		Integer yearsToRetire;

		Double ratioOnTimeLegs;

		Double ratioDelayedLegs;

		Airport mostPopularAirport;

		Airport lessPopularAirport;

		int numberOfLegsLanded;

		int numberOfLegsCancelled;

		int numberOfLegsDelayed;

		int numberOfLegsOnTime;

		Money avg;

		Money min;

		Money max;

		Money std;

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		SystemConfiguration config = this.dashboardRepository.getSystemConfiguration();
		String currency = config.getSystemCurrency();

		rankingPosition = this.getRankingPosition(managerId);
		yearsToRetire = this.getYearsToRetire(managerId);

		ratioOnTimeLegs = this.calculateOnTimeLegRatio(managerId);
		ratioDelayedLegs = this.calculateDelayedLegRatio(managerId);

		mostPopularAirport = this.getMostPopularAirport(managerId);
		lessPopularAirport = this.getLeastPopularAirport(managerId);

		avg = this.createMoney(currency, this.getCostStat(managerId, 0)).getAmount() == null ? null : this.createMoney(currency, this.getCostStat(managerId, 0));
		min = this.createMoney(currency, this.getCostStat(managerId, 1)).getAmount() == null ? null : this.createMoney(currency, this.getCostStat(managerId, 1));
		max = this.createMoney(currency, this.getCostStat(managerId, 2)).getAmount() == null ? null : this.createMoney(currency, this.getCostStat(managerId, 2));
		std = this.createMoney(currency, this.getCostStat(managerId, 3)).getAmount() == null ? null : this.createMoney(currency, this.getCostStat(managerId, 3));

		numberOfLegsOnTime = this.dashboardRepository.countOnTimeLegs(managerId);
		numberOfLegsDelayed = this.dashboardRepository.countDelayedLegs(managerId);
		numberOfLegsCancelled = this.dashboardRepository.countCancelledLegs(managerId);
		numberOfLegsLanded = this.dashboardRepository.countLandedLegs(managerId);

		dashboard.setRankingPosition(rankingPosition);
		dashboard.setYearsToRetire(yearsToRetire);
		dashboard.setRatioOnTimeLegs(ratioOnTimeLegs);
		dashboard.setRatioDelayedLegs(ratioDelayedLegs);
		dashboard.setAvgDesviationOfCost(avg);
		dashboard.setMinDesviationOfCost(min);
		dashboard.setMaxDesviationOfCost(max);
		dashboard.setStandardDesviationOfCost(std);
		dashboard.setLeastPopularAirport(lessPopularAirport);
		dashboard.setMostPopularAirport(mostPopularAirport);
		dashboard.setNumberOfLegsCancelled(numberOfLegsCancelled);
		dashboard.setNumberOfLegsDelayed(numberOfLegsDelayed);
		dashboard.setNumberOfLegsLanded(numberOfLegsLanded);
		dashboard.setNumberOfLegsOnTime(numberOfLegsOnTime);
		super.getBuffer().addData(dashboard);

	}

	private Integer getRankingPosition(final int managerId) {
		return this.getManagerRanking(managerId);
	}

	public Integer getYearsToRetire(final int managerId) {
		Date dateOfBirth = this.dashboardRepository.findManagerById(managerId).getDateOfBirth();

		Calendar birthCalendar = Calendar.getInstance();
		birthCalendar.setTime(dateOfBirth);

		Calendar currentCalendar = Calendar.getInstance();

		int birthYear = birthCalendar.get(Calendar.YEAR);
		int currentYear = currentCalendar.get(Calendar.YEAR);

		int age = currentYear - birthYear;

		if (currentCalendar.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR))
			age--;

		return Math.max(65 - age, 0);
	}

	private Double calculateOnTimeLegRatio(final int managerId) {
		int onTime = this.dashboardRepository.countOnTimeLegs(managerId);
		int delayed = this.dashboardRepository.countDelayedLegs(managerId);
		int cancelled = this.dashboardRepository.countCancelledLegs(managerId);
		int landed = this.dashboardRepository.countLandedLegs(managerId);
		int total = onTime + delayed + cancelled + landed;
		return total > 0 ? (double) onTime / total : null;
	}

	private Double calculateDelayedLegRatio(final int managerId) {
		int onTime = this.dashboardRepository.countOnTimeLegs(managerId);
		int delayed = this.dashboardRepository.countDelayedLegs(managerId);
		int cancelled = this.dashboardRepository.countCancelledLegs(managerId);
		int landed = this.dashboardRepository.countLandedLegs(managerId);
		int total = onTime + delayed + cancelled + landed;
		return total > 0 ? (double) delayed / total : null;
	}

	private Airport getMostPopularAirport(final int managerId) {
		return this.mostPopularAirport(managerId);
	}

	private Airport getLeastPopularAirport(final int managerId) {
		return this.leastPopularAirport(managerId);
	}

	private Double getCostStat(final int managerId, final int index) {
		Object[] stats = this.dashboardRepository.statsCost(managerId).get(0);
		return (Double) stats[index];
	}

	private Money createMoney(final String currency, final Double amount) {
		Money money = new Money();
		money.setCurrency(currency);
		money.setAmount(amount);
		return money;
	}

	private Integer getManagerRanking(final int managerId) {
		List<Manager> orderedManagers = this.dashboardRepository.findAllOrderedByExperience();
		for (int i = 0; i < orderedManagers.size(); i++)
			if (orderedManagers.get(i).getId() == managerId)
				return i + 1;
		return -1;
	}

	private Airport mostPopularAirport(final int managerId) {
		List<Flight> flights = this.dashboardRepository.findFlightsByManagerId(managerId);
		Map<Airport, Integer> airportsPopularities = new HashMap<>();
		for (Flight flight : flights) {
			List<Leg> flightLegs = this.dashboardRepository.findLegsByFlightId(flight.getId());

			if (!flightLegs.isEmpty()) {
				Leg firstLeg = flightLegs.get(0);
				Leg lastLeg = flightLegs.get(flightLegs.size() - 1);

				Airport departureAirport = firstLeg.getDepartureAirport();
				Airport arrivalAirport = lastLeg.getArrivalAirport();

				airportsPopularities.put(departureAirport, airportsPopularities.getOrDefault(departureAirport, 0) + 1);

				airportsPopularities.put(arrivalAirport, airportsPopularities.getOrDefault(arrivalAirport, 0) + 1);
			}

		}

		Airport mostPopularAirport = null;
		int maxPopularity = 0;
		for (Map.Entry<Airport, Integer> entry : airportsPopularities.entrySet())
			if (entry.getValue() > maxPopularity) {
				mostPopularAirport = entry.getKey();
				maxPopularity = entry.getValue();
			}
		return mostPopularAirport;
	}

	private Airport leastPopularAirport(final int managerId) {

		List<Flight> flights = this.dashboardRepository.findFlightsByManagerId(managerId);

		Map<Airport, Integer> airportsPopularities = new HashMap<>();

		for (Flight flight : flights) {

			List<Leg> flightLegs = this.dashboardRepository.findLegsByFlightId(flight.getId());

			if (!flightLegs.isEmpty()) {

				Leg firstLeg = flightLegs.get(0);
				Leg lastLeg = flightLegs.get(flightLegs.size() - 1);

				Airport departureAirport = firstLeg.getDepartureAirport();
				Airport arrivalAirport = lastLeg.getArrivalAirport();

				airportsPopularities.put(departureAirport, airportsPopularities.getOrDefault(departureAirport, 0) + 1);

				airportsPopularities.put(arrivalAirport, airportsPopularities.getOrDefault(arrivalAirport, 0) + 1);

			}

		}

		// Determinamos el aeropuerto menos popular
		Airport leastPopularAirport = null;
		int minPopularity = Integer.MAX_VALUE;

		for (Map.Entry<Airport, Integer> entry : airportsPopularities.entrySet())
			if (entry.getValue() < minPopularity) {
				leastPopularAirport = entry.getKey();
				minPopularity = entry.getValue();
			}

		return leastPopularAirport;
	}

	@Override
	public void unbind(final ManagerDashboard dashboard) {

		Dataset dataset;

		dataset = super.unbindObject(dashboard, "rankingPosition", "yearsToRetire", "ratioOnTimeLegs", "ratioDelayedLegs", "numberOfLegsLanded", "numberOfLegsCancelled", "numberOfLegsDelayed", "numberOfLegsOnTime", "avgDesviationOfCost",
			"minDesviationOfCost", "maxDesviationOfCost", "standardDesviationOfCost");

		String iataCodeMostPopularAirport = dashboard.getMostPopularAirport() != null ? dashboard.getMostPopularAirport().getIataCode() : null;
		String iataCodeLeastPopularAirport = dashboard.getLeastPopularAirport() != null ? dashboard.getLeastPopularAirport().getIataCode() : null;

		dataset.put("mostPopularAirport", iataCodeMostPopularAirport);
		dataset.put("leastPopularAirport", iataCodeLeastPopularAirport);

		super.getResponse().addData(dataset);

	}

}
