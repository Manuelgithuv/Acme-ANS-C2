
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airline.AirlineRepository;
import acme.entities.leg.LegRepository;

@Validator
public class FlightCodeValidator extends AbstractValidator<ValidFlightCode, String> {

	private final LegRepository		legRepository;
	private final AirlineRepository	airlineRepository;


	@Autowired
	public FlightCodeValidator(final LegRepository legRepository, final AirlineRepository airlineRepository) {
		this.legRepository = legRepository;
		this.airlineRepository = airlineRepository;
	}

	@Override
	protected void initialise(final ValidFlightCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String flightCode, final ConstraintValidatorContext context) {

		boolean result;
		if (flightCode == null || flightCode.isBlank())
			super.state(context, false, "flight code", "Flight code cannot be empty");

		// Extraer el código de aerolínea (los caracteres iniciales antes de los 4 dígitos finales)
		String airlineCode = flightCode.substring(0, flightCode.length() - 4);

		// Verificar si la aerolínea existe
		if (this.airlineRepository.countByAirlineCode(airlineCode) == 0)
			super.state(context, false, "flight code", "Airline does not exist");

		// Verificar si el código de vuelo ya existe
		if (this.legRepository.existsByFlightCode(flightCode))
			super.state(context, false, "flight code", "Flight code already exists");

		result = !super.hasErrors(context);
		return result;
	}

}
