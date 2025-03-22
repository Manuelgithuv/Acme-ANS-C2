
package acme.constraints;

import java.util.Comparator;
import java.util.Date;
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

		List<Leg> legs = this.legRepository.findDistinctByFlight(leg.getFlight().getId());
		if(!legs.contains(leg)) {
			legs.add(leg);
		}
		List<Leg> sortedLegs = legs.stream()
		    .sorted(Comparator.comparing(Leg::getScheduledDeparture))
		    .toList();
		for (int i = 0; i < sortedLegs.size() - 1; i++) {
			Leg currentLeg = sortedLegs.get(i);
			Leg nextLeg = sortedLegs.get(i + 1);

			if (this.areLegsConsecutive(currentLeg, nextLeg) || !this.areAirportsConcording(currentLeg, nextLeg))
				super.state(context, false, "*", "consecutive.legs.diferent.moments.and.airports");
		}

		result = !super.hasErrors(context);
		return result;
	}

	private boolean areLegsConsecutive(final Leg currentLeg, final Leg nextLeg) {

		Date currentArrival = currentLeg.getScheduledArrival();
		Date nextDeparture = nextLeg.getScheduledDeparture();

		long currentArrivalInMinutes = currentArrival.getTime() / 60000;
		long nextDepartureInMinutes = nextDeparture.getTime() / 60000;

		return currentArrivalInMinutes == nextDepartureInMinutes;
	}
	private boolean areAirportsConcording(final Leg currentLeg, final Leg nextLeg) {
		
		return currentLeg.getArrivalAirport().getIataCode().equals(nextLeg.getDepartureAirport().getIataCode());
	}

}
