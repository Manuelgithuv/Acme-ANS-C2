
package acme.constraints;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.leg.Leg;
import acme.features.manager.leg.LegRepository;

public class TimeBetweenLegsValidator extends AbstractValidator<ValidTimeBetweenConsecutiveLegs, Leg> {

	private final LegRepository legRepository;


	@Autowired
	public TimeBetweenLegsValidator(final LegRepository legRepository) {
		this.legRepository = legRepository;
	}

	@Override
	protected void initialise(final ValidTimeBetweenConsecutiveLegs annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {

		boolean result;

		// Validar si 'leg' o 'leg.getFlight()' son null antes de operar sobre ellos
		if (leg == null || leg.getFlight() == null) {
			super.state(context, false, "flight", "flight.cannot.be.null");
			return false;
		}

		List<Leg> legs = this.legRepository.findDistinctByFlight(leg.getFlight().getId());

		if (legs == null)
			legs = new ArrayList<>();

		if (legs.stream().noneMatch(l -> l.getId() == leg.getId()))
			legs.add(leg);

		// Filtrar cualquier elemento nulo antes de ordenar
		List<Leg> sortedLegs = legs.stream().filter(l -> l != null && l.getScheduledDeparture() != null) // Filtrar nulos
			.sorted(Comparator.comparing(Leg::getScheduledDeparture, Comparator.nullsLast(Comparator.naturalOrder()))).toList();

		for (int i = 0; i < sortedLegs.size() - 1; i++) {
			Leg currentLeg = sortedLegs.get(i);
			Leg nextLeg = sortedLegs.get(i + 1);

			if (currentLeg == null || nextLeg == null)
				continue; // Evitar procesar elementos nulos

			if (this.areLegsConsecutive(currentLeg, nextLeg))
				super.state(context, false, "scheduledDeparture", "consecutive.legs.must.be.different.moments");
			if (!this.areAirportsConcording(currentLeg, nextLeg))
				super.state(context, false, "departureAirport", "consecutive.legs.must.have.same.arrival-departure-airports");
		}

		result = !super.hasErrors(context);
		return result;
	}

	private boolean areLegsConsecutive(final Leg currentLeg, final Leg nextLeg) {
		if (currentLeg == null || nextLeg == null || currentLeg.getScheduledArrival() == null || nextLeg.getScheduledDeparture() == null)
			return false;

		long currentArrivalInMinutes = currentLeg.getScheduledArrival().getTime() / 60000;
		long nextDepartureInMinutes = nextLeg.getScheduledDeparture().getTime() / 60000;

		return currentArrivalInMinutes == nextDepartureInMinutes;
	}

	private boolean areAirportsConcording(final Leg currentLeg, final Leg nextLeg) {
		if (currentLeg == null || nextLeg == null || currentLeg.getArrivalAirport() == null || nextLeg.getDepartureAirport() == null)
			return false;

		return currentLeg.getArrivalAirport().getIataCode().equals(nextLeg.getDepartureAirport().getIataCode());
	}

}
