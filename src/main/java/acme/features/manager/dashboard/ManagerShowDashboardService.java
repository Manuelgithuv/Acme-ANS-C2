package acme.features.manager.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.forms.ManagerDashboard;
import acme.realms.Manager;

@GuiService
public class ManagerShowDashboardService extends AbstractGuiService<Manager, ManagerDashboard> {
	
	@Autowired
	private ManagerDashboardRepository dashboardRepository;
	
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
		
	}
	
	@Override
	public void load() {
		
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		
		ManagerDashboard dashboard;
		
		Integer	rankingPosition;

		Integer yearsToRetire;

		 Double ratioOnTimeLegs;

		 Double	ratioDelayedLegs;

		 Airport	mostPopularAirport;

		 Airport	lessPopularAirport;

		 Integer numberOfLegsPending;
		
		 Integer numberOfLegsCancelled;
		
		 Integer numberOfLegsDelayed;
		
		 Integer numberOfLegsOnTTime;

		 Double	averageCostDesviationOfFlights;

		 Double	minimumCostDesviationOfFlights;

		 Double	maximumCostDesviationOfFlights;

		 Double	standardCostDesviationOfFlights;
		 
		 rankingPosition = getManagerRanking(managerId);
		 
		 yearsToRetire = this.dashboardRepository.getYearsToRetirement(managerId);
		 
		 ratioOnTimeLegs = this.dashboardRepository.getOnTimeRatio(managerId);
		 
		 ratioDelayedLegs = this.dashboardRepository.getDelayedRatio(managerId);
		 
		 mostPopularAirport = this.mostPopularAirport(managerId);
		 
		 lessPopularAirport = this.leastPopularAirport(managerId);
		
	}
	
	private Integer getManagerRanking(int managerId) {
	    List<Manager> orderedManagers = this.dashboardRepository.findAllOrderedByExperience();
	    for (int i = 0; i < orderedManagers.size(); i++) {
	        if (orderedManagers.get(i).getId()==(managerId)) {
	            return i + 1; 
	        }
	    }
	    return -1;
	}
	
	private Airport mostPopularAirport(int managerId) {
		List<Flight> flights = this.dashboardRepository.findFlightsByManagerId(managerId);
		Map<Airport, Integer> airportsPopularities = new HashMap<Airport, Integer>();
		for(Flight flight:flights) {
			List<Leg> flightLegs = this.dashboardRepository.findLegsByFlightId(flight.getId());
			
			Leg firstLeg = flightLegs.get(0);
			Leg lastLeg = flightLegs.get(flightLegs.size()-1);
			
			Airport departureAirport = firstLeg.getDepartureAirport();
	        Airport arrivalAirport = lastLeg.getArrivalAirport();

	        
	        airportsPopularities.put(departureAirport, airportsPopularities.getOrDefault(departureAirport, 0) + 1);

	        airportsPopularities.put(arrivalAirport, airportsPopularities.getOrDefault(arrivalAirport, 0) + 1);
			
		}
		
		Airport mostPopularAirport = null;
	    int maxPopularity = 0;
	    for (Map.Entry<Airport, Integer> entry : airportsPopularities.entrySet()) {
	        if (entry.getValue() > maxPopularity) {
	            mostPopularAirport = entry.getKey();
	            maxPopularity = entry.getValue();
	        }
	    }
	    return mostPopularAirport;
	}
	
	private Airport leastPopularAirport(int managerId) {
	    
	    List<Flight> flights = this.dashboardRepository.findFlightsByManagerId(managerId);

	    
	    Map<Airport, Integer> airportsPopularities = new HashMap<>();

	    
	    for (Flight flight : flights) {
	        
	        List<Leg> flightLegs = this.dashboardRepository.findLegsByFlightId(flight.getId());

	        
	        Leg firstLeg = flightLegs.get(0);
	        Leg lastLeg = flightLegs.get(flightLegs.size() - 1);

	        
	        Airport departureAirport = firstLeg.getDepartureAirport();
	        Airport arrivalAirport = lastLeg.getArrivalAirport();

	       
	        airportsPopularities.put(departureAirport, airportsPopularities.getOrDefault(departureAirport, 0) + 1);

	        
	        airportsPopularities.put(arrivalAirport, airportsPopularities.getOrDefault(arrivalAirport, 0) + 1);
	    }

	    // Determinamos el aeropuerto menos popular
	    Airport leastPopularAirport = null;
	    int minPopularity = Integer.MAX_VALUE;

	    for (Map.Entry<Airport, Integer> entry : airportsPopularities.entrySet()) {
	        if (entry.getValue() < minPopularity) {
	            leastPopularAirport = entry.getKey();
	            minPopularity = entry.getValue();
	        }
	    }

	    return leastPopularAirport;
	}


	
	@Override
	public void unbind(final ManagerDashboard dashboard) {
		
	}

}
