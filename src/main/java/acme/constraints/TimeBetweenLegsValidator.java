
package acme.constraints;

import java.util.Date;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.leg.Leg;
import acme.entities.leg.LegRepository;

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

		for (int i = 0; i < legs.size() - 1; i++) {
			Leg currentLeg = legs.get(i);
			Leg nextLeg = legs.get(i + 1);

			if (this.areLegsConsecutive(currentLeg, nextLeg))
				super.state(context, false, "Consecutive Legs", "Consecutive legs of a flight can not be at the same moment");
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

}
