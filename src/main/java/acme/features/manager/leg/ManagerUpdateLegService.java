
package acme.features.manager.leg;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

		Optional<Leg> existingLeg = this.legRepository.findByFlightCode(leg.getFlightCode());

		if (!existingLeg.isEmpty() && !leg.getFlightCode().equals(existingLeg.get().getFlightCode()))
			super.state(false, "flightCode", "manager.leg.flightCode.alreadyExists");
		if (leg.getAircraft() != null)
			if (leg.getAircraft().getStatus().equals(AircraftStatus.UNDER_MAINTENANCE))
				super.state(false, "aircraft", "leg.aircraft.is-in-maintenance");

		boolean status;

		// Validar si los aeropuertos de leg son nulos antes de acceder a sus IDs
		Airport departureAirport = leg.getDepartureAirport() != null ? this.airportRepository.findById(leg.getDepartureAirport().getId()) : null;

		Airport arrivalAirport = leg.getArrivalAirport() != null ? this.airportRepository.findById(leg.getArrivalAirport().getId()) : null;

		// Validar si los aeropuertos encontrados son nulos antes de comparar IDs
		boolean areAirportsEquals = departureAirport != null && arrivalAirport != null &&

			departureAirport.getId() == arrivalAirport.getId();

		// Validar si scheduledDeparture y scheduledArrival son nulos antes de llamar a before()
		Date scheduledDeparture = leg.getScheduledDeparture();
		Date scheduledArrival = leg.getScheduledArrival();

		boolean isDepartureBeforeArrival = scheduledDeparture != null && scheduledArrival != null && scheduledDeparture.before(scheduledArrival);

		status = isDepartureBeforeArrival;

		if (areAirportsEquals)
			super.state(false, "*", "manager.leg.create.airports");

		if (leg.getFlight() != null) {
			boolean res = this.validateTimeAndAirportsInConsecutiveLegs(leg);
			super.state(res, "*", "manager.consecutive.legs.invalid.dates-or-airports");
		}

		if (leg.getAircraft() != null) {
			boolean validAircraft = this.validateAircraftNotInUse(leg);
			super.state(validAircraft, "aircraft", "leg.aircraft.in-use.for.that.period.of.time");
		}

		if (leg.getScheduledDeparture() != null) {
			long actualUpperLimit = MomentHelper.getCurrentMoment().getTime() / 60000;
			long departureInMinutes = leg.getScheduledDeparture().getTime() / 60000;
			if (departureInMinutes < actualUpperLimit)
				super.state(false, "scheduledDeparture", "departure.minimum.currentDate");
		}

		super.state(status, "*", "manager.leg.create.dates");
	}

	private boolean validateTimeAndAirportsInConsecutiveLegs(final Leg leg) {

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

				Airport currentAirport = currentLeg.getArrivalAirport();
				Airport nextAirport = nextLeg.getDepartureAirport();

				if (currentArrivalInMinutes >= nextDepartureInMinutes || currentAirport.getId() != nextAirport.getId())
					res = false;
				else
					res = true;
			}
		}
		return res;
	}
	private boolean validateAircraftNotInUse(final Leg leg) {
		boolean res = true;

		List<Leg> legsDistincFromActualFlight = this.legRepository.findByFlightIdNot(leg.getFlight().getId()).stream()
			.filter(l -> (l.getScheduledDeparture().after(leg.getScheduledDeparture()) || l.getScheduledDeparture().equals(leg.getScheduledDeparture()))
				&& (l.getScheduledArrival().before(leg.getScheduledArrival()) || l.getScheduledArrival().equals(leg.getScheduledArrival())))
			.collect(Collectors.toList());

		for (Leg l : legsDistincFromActualFlight)
			if (l.getAircraft().getId() == leg.getAircraft().getId() && !l.isPublished()) {
				res = false;
				break;
			}
		return res;

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
