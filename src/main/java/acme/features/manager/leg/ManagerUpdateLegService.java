
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.datatypes.AircraftStatus;
import acme.datatypes.LegStatus;
import acme.entities.aircraft.Aircraft;
import acme.entities.aircraft.AircraftRepository;
import acme.entities.airport.Airport;
import acme.entities.airport.AirportRepository;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@GuiService
public class ManagerUpdateLegService extends AbstractGuiService<Manager, Leg> {

	@Autowired
	private LegRepository		legRepository;

	@Autowired
	private AirportRepository	airportRepository;

	@Autowired
	private AircraftRepository	aircraftRepository;


	@Override
	public void authorise() {
		boolean status;

		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		int id = super.getRequest().getData("id", int.class);

		Leg leg = this.legRepository.findById(id);

		status = leg != null && leg.getManager().getId() == managerId && !leg.getFlight().isPublished() && !leg.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		Leg leg;

		int id;

		id = super.getRequest().getData("id", int.class);

		leg = this.legRepository.findById(id);

		super.getBuffer().addData(leg);

	}

	@Override
	public void bind(final Leg leg) {

		int aircraftId;
		Aircraft aircraft;

		int departureAirportId;
		Airport departureAirport;

		int arrivalAirportId;
		Airport arrivalAirport;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.aircraftRepository.findById(aircraftId);

		departureAirportId = super.getRequest().getData("departureAirport", int.class);
		departureAirport = this.airportRepository.findById(departureAirportId);

		arrivalAirportId = super.getRequest().getData("arrivalAirport", int.class);
		arrivalAirport = this.airportRepository.findById(arrivalAirportId);

		super.bindObject(leg, "flightCode", "scheduledDeparture", "scheduledArrival", "status", "hours");

		leg.setAircraft(aircraft);
		leg.setDepartureAirport(departureAirport);
		leg.setArrivalAirport(arrivalAirport);

	}

	@Override
	public void validate(final Leg leg) {
	    validateFlightCode(leg);
	    validateAircraftStatus(leg);
	    validateAirports(leg);
	    validateDates(leg);
	    validateConsecutiveLegs(leg);
	    validateAircraftUsage(leg);
	    validateScheduledDeparture(leg);
	}

	private void validateFlightCode(final Leg leg) {
	    Optional<Leg> existingLeg = this.legRepository.findByFlightCode(leg.getFlightCode());
	    if (!existingLeg.isEmpty() && existingLeg.get().getId()!=leg.getId()) {
	        super.state(false, "flightCode", "manager.leg.flightCode.alreadyExists");
	    }
	}

	private void validateAircraftStatus(final Leg leg) {
	    if (leg.getAircraft() != null && leg.getAircraft().getStatus() != null 
	        && leg.getAircraft().getStatus().equals(AircraftStatus.UNDER_MAINTENANCE)) {
	        super.state(false, "aircraft", "leg.aircraft.is-in-maintenance");
	    }
	}

	private void validateAirports(final Leg leg) {
	    Airport departureAirport = leg.getDepartureAirport() != null ? 
	        this.airportRepository.findById(leg.getDepartureAirport().getId()) : null;
	    Airport arrivalAirport = leg.getArrivalAirport() != null ? 
	        this.airportRepository.findById(leg.getArrivalAirport().getId()) : null;

	    if (departureAirport != null && arrivalAirport != null && 
	        departureAirport.getId() == arrivalAirport.getId()) {
	        super.state(false, "*", "manager.leg.create.airports");
	    }
	}

	private void validateDates(final Leg leg) {
	    Date scheduledDeparture = leg.getScheduledDeparture();
	    Date scheduledArrival = leg.getScheduledArrival();

	    boolean isDepartureBeforeArrival = scheduledDeparture != null && scheduledArrival != null 
	        && scheduledDeparture.before(scheduledArrival);

	    super.state(isDepartureBeforeArrival, "*", "manager.leg.create.dates");
	}

	private void validateConsecutiveLegs(final Leg leg) {
	    if (leg.getFlight() != null) {
	        boolean res = this.validateTimeInConsecutiveLegs(leg);
	        super.state(res, "*", "manager.consecutive.legs.invalid.dates");
	    }
	}

	private void validateAircraftUsage(final Leg leg) {
	    if (leg.getAircraft() != null && leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
	        boolean validAircraft = this.validateAircraftNotInUse(leg);
	        super.state(validAircraft, "aircraft", "leg.aircraft.in-use.for.that.period.of.time");
	    }
	}

	private void validateScheduledDeparture(final Leg leg) {
	    if (leg.getScheduledDeparture() != null) {
	        long actualUpperLimit = MomentHelper.getCurrentMoment().getTime() / 60000;
	        long departureInMinutes = leg.getScheduledDeparture().getTime() / 60000;
	        
	        if (departureInMinutes < actualUpperLimit) {
	            super.state(false, "scheduledDeparture", "departure.minimum.currentDate");
	        }
	    }
	}

	private boolean validateTimeInConsecutiveLegs(final Leg leg) {

		boolean res = false;
		List<Leg> legs = this.legRepository.findDistinctByFlight(leg.getFlight().getId());

		if (leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null && leg.getArrivalAirport() != null && leg.getDepartureAirport() != null) {
			for (int i = 0; i < legs.size(); i++)
				if (legs.get(i).getId() == leg.getId()) {
					legs.set(i, leg);
					break;
				}
			legs.sort(Comparator.comparing(Leg::getScheduledDeparture));
			for (int i = 0; i < legs.size() - 1; i++) {

				Leg currentLeg = legs.get(i);
				Leg nextLeg = legs.get(i + 1);

				long currentArrivalInMinutes = currentLeg.getScheduledArrival().getTime() / 60000;
				long nextDepartureInMinutes = nextLeg.getScheduledDeparture().getTime() / 60000;
				
				long currentDepartureInMinutes = currentLeg.getScheduledDeparture().getTime() / 60000;
				long nextArrivalInMinutes =  nextLeg.getScheduledArrival().getTime() / 60000;


				if (currentArrivalInMinutes >= nextDepartureInMinutes || currentDepartureInMinutes==nextDepartureInMinutes || currentArrivalInMinutes==nextArrivalInMinutes)
					res = false;
				else
					res = true;
			}
		}
		return res;
	}
	private boolean validateAircraftNotInUse(final Leg leg) {
	    List<Leg> legs = this.legRepository.findLegsByFlightIdNotAndAircraftIdAndPublished(
	        leg.getFlight().getId(), leg.getAircraft().getId()
	    );

	    long actualDepartureInMinutes = leg.getScheduledDeparture().getTime() / 60000;
	    long actualArrivalInMinutes = leg.getScheduledArrival().getTime() / 60000;
	    long marginInMinutes = 1; // Margen de tiempo en minutos

	    for (Leg l : legs) {
	        long departureInMinutes = l.getScheduledDeparture().getTime() / 60000;
	        long arrivalInMinutes = l.getScheduledArrival().getTime() / 60000;

	        // Comprobar si hay solapamiento con margen de tiempo
	        if (!(actualArrivalInMinutes + marginInMinutes <= departureInMinutes || 
	              actualDepartureInMinutes - marginInMinutes >= arrivalInMinutes)) {
	            return false;
	        }
	    }
	    return true;
	}

	@Override
	public void perform(final Leg leg) {
		this.legRepository.save(leg);

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset = this.buildDataset(leg);

		this.populateDatasetWithChoices(dataset, leg);

		super.getResponse().addData(dataset);
	}

	private Dataset buildDataset(final Leg leg) {
		return super.unbindObject(leg, "flightCode", "scheduledDeparture", "scheduledArrival", "status", "hours", "published");
	}

	private void populateDatasetWithChoices(final Dataset dataset, final Leg leg) {
		Collection<Airport> airports = this.airportRepository.findAllAirports();
		Collection<Aircraft> aircrafts = this.aircraftRepository.findAllActiveAircrafts();

		SelectChoices departureAirportChoices = SelectChoices.from(airports, "iataCode", leg.getDepartureAirport());
		SelectChoices arrivalAirportChoices = SelectChoices.from(airports, "iataCode", leg.getArrivalAirport());
		Aircraft aircraft = leg.getAircraft() == null || leg.getAircraft().getId() == 0 ? null : leg.getAircraft();
		SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", aircraft);
		SelectChoices statusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());

		dataset.put("departureAirport", departureAirportChoices.getSelected().getKey());
		dataset.put("departureAirports", departureAirportChoices);
		dataset.put("arrivalAirport", arrivalAirportChoices.getSelected().getKey());
		dataset.put("arrivalAirports", arrivalAirportChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("statuses", statusChoices);

	}

}
