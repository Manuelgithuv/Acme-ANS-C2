
package acme.constraints;

import java.time.Year;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.service.Service;

@Validator
public class PromotionCodeValidator extends AbstractValidator<ValidPromotionCode, Service> {

	@Override
	protected void initialise(final ValidPromotionCode annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Service value, final ConstraintValidatorContext context) {
		String input = value.getPromotionCode();
		if (input != null) {

			String[] partes = input.split("-");
			String numeroStr = partes[1];
			// Obtener los últimos dos dígitos del año actual
			int anioActual = Year.now().getValue();
			int ultimosDosDigitos = anioActual % 100;

			// Comparar con la parte extraída
			Boolean result = numeroStr.equals(String.valueOf(ultimosDosDigitos));
			if (!result)
				super.state(context, false, "promotion code", "The last two digits are not current year");
		}
		return !super.hasErrors(context);
	}

}
