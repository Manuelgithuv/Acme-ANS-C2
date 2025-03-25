
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.Manager;

@Validator
public class ManagerIdentifierNumberValidator extends AbstractValidator<ValidManagerIdentifierNumber, Manager> {

	@Override
	protected void initialise(final ValidManagerIdentifierNumber annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Manager manager, final ConstraintValidatorContext context) {
		String name = manager.getIdentity().getName();
		String surname = manager.getIdentity().getSurname();
		String identifierNumber = manager.getIdentifierNumber();
		// Inicializar las iniciales por si name o surname son null y que no se produzca null pointer exception
		char nameInitial = '-';
		char surnameInitial = '-';
		// Validar nombre
		if (name == null || name.isBlank())
			super.state(context, false, "name", "name.does.not.exist");
		else {
			name = name.trim().toUpperCase();
			nameInitial = name.charAt(0);
		}

		// Validar apellido
		if (surname == null || surname.isBlank())
			super.state(context, false, "surname", "surname.does.not.exist");
		else {
			surname = surname.trim().toUpperCase();
			surnameInitial = surname.charAt(0);
		}

		identifierNumber = identifierNumber.trim();

		// Comparar iniciales con el identificador
		if (identifierNumber == null || identifierNumber.isBlank() || identifierNumber.charAt(0) != nameInitial || identifierNumber.charAt(1) != surnameInitial)
			super.state(context, false, "identifierNumber", "the.first.two.digits.are.not.the.initials");

		return !super.hasErrors(context);
	}

}
