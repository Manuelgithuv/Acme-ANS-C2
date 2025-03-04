
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.airline.AirlineRepository;
import acme.entities.leg.Leg;
import acme.entities.leg.LegRepository;

@Validator
public class FlightCodeValidator extends AbstractValidator<ValidFlightCode, Leg> {

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
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {

		boolean result;
		if (leg.getFlightCode() == null || leg.getFlightCode().isBlank())
			super.state(context, false, "flight code", "Flight code cannot be empty");

		// Extraer el código de aerolínea (los caracteres iniciales antes de los 4 dígitos finales)
		String flightCode = leg.getFlightCode();
		String airlineCode = flightCode != null && flightCode.length() >= 4 ? flightCode.substring(0, flightCode.length() - 4) : null;

		// Verificar si la aerolínea existe
		if (this.airlineRepository.countByAirlineCode(airlineCode) == 0)
			super.state(context, false, "flight code", "Airline does not exist");
		// Verificar si es el Iata code de la aerolinea asociada
		if (!leg.getAircraft().getAirline().getIataCode().equals(airlineCode))

			super.state(context, false, "flight code", "The firt digits must be the IATA code of the associated airline");

		// Verificar si el código de vuelo ya existe
		if (this.legRepository.existsByFlightCode(flightCode))
			super.state(context, false, "flight code", "Flight code already exists");

		result = !super.hasErrors(context);
		return result;
	}

}
