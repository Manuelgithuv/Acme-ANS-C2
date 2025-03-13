
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.realms.Customer;

public class CustomerIdentifierValidator extends AbstractValidator<ValidCustomerIdentifierNumber, Customer> {

	@Override
	protected void initialise(final ValidCustomerIdentifierNumber annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Customer customer, final ConstraintValidatorContext context) {
		String name = customer.getIdentity().getName();
		String surname = customer.getIdentity().getSurname();
		String identifierNumber = customer.getIdentifier();
		// Inicializar las iniciales por si name o surname son null y que no se produzca null pointer exception
		char nameInitial = '-';
		char surnameInitial = '-';
		// Validar nombre
		if (name == null || name.isBlank())
			super.state(context, false, "identifier number", "Name does not exist");
		else {
			name = name.trim().toUpperCase();
			nameInitial = name.charAt(0);
		}

		// Validar apellido
		if (surname == null || surname.isBlank())
			super.state(context, false, "identifier number", "Surname does not exist");
		else {
			surname = surname.trim().toUpperCase();
			surnameInitial = surname.charAt(0);
		}

		identifierNumber = identifierNumber.trim();

		// Comparar iniciales con el identificador
		if (identifierNumber.charAt(0) != nameInitial || identifierNumber.charAt(1) != surnameInitial)
			super.state(context, false, "identifier number", "The first two digits are not the initials");

		return !super.hasErrors(context);
	}

}
