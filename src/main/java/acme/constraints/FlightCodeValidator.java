
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airline.AirlineRepository;
import acme.entities.leg.Leg;

@Validator
public class FlightCodeValidator extends AbstractValidator<ValidFlightCode, Leg> {

	private final AirlineRepository airlineRepository;


	@Autowired
	public FlightCodeValidator(final AirlineRepository airlineRepository) {

		this.airlineRepository = airlineRepository;
	}

	@Override
	protected void initialise(final ValidFlightCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {

		boolean result;

		// Validar si leg o flightCode son null antes de operar sobre ellos
		if (leg == null || leg.getFlightCode() == null) {
			super.state(context, false, "flightCode", "flight.code.cannot.be.null");
			return false;
		}

		// Extraer el código de aerolínea (los caracteres iniciales antes de los 4 dígitos finales)
		String flightCode = leg.getFlightCode();
		String airlineCode = flightCode.length() >= 4 ? flightCode.substring(0, flightCode.length() - 4) : null;

		// Verificar si la aerolínea existe
		if (airlineCode == null || this.airlineRepository.countByAirlineCode(airlineCode) == 0)
			super.state(context, false, "aircraft.airline", "airline.does.not.exist");

		// Validar que leg.getAircraft() y leg.getAircraft().getAirline() no sean null
		if (leg.getAircraft() == null || leg.getAircraft().getAirline() == null)
			super.state(context, false, "flightCode", "aircraft.or.airline.cannot.be.null");
		else if (!leg.getAircraft().getAirline().getIataCode().equals(airlineCode))
			super.state(context, false, "flightCode", "the.first.digits.must.be.the.IATA.code.of.the.associated.airline");

		result = !super.hasErrors(context);
		return result;
	}

}
