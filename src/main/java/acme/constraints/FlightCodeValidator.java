
package acme.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import acme.entities.airline.AirlineRepository;
import acme.entities.leg.LegRepository;

@Component
public class FlightCodeValidator implements ConstraintValidator<ValidFlightCode, String> {

	private final LegRepository		legRepository;
	private final AirlineRepository	airlineRepository;


	@Autowired
	public FlightCodeValidator(final LegRepository legRepository, final AirlineRepository airlineRepository) {
		this.legRepository = legRepository;
		this.airlineRepository = airlineRepository;
	}

	@Override
	public boolean isValid(final String flightCode, final ConstraintValidatorContext context) {
		if (flightCode == null || flightCode.isBlank()) {
			context.buildConstraintViolationWithTemplate("Flight code cannot be empty").addConstraintViolation();
			return false;
		}

		// Extraer el código de aerolínea (los caracteres iniciales antes de los 4 dígitos finales)
		String airlineCode = flightCode.substring(0, flightCode.length() - 4);

		// Verificar si la aerolínea existe
		if (this.airlineRepository.countByAirlineCode(airlineCode) == 0) {
			context.buildConstraintViolationWithTemplate("Airline does not exist").addPropertyNode("flightCode").addConstraintViolation();
			return false;
		}

		// Verificar si el código de vuelo ya existe
		if (this.legRepository.existsByFlightCode(flightCode)) {
			context.buildConstraintViolationWithTemplate("Flight code already exists").addPropertyNode("flightCode").addConstraintViolation();
			return false;
		}

		return true;
	}

}
