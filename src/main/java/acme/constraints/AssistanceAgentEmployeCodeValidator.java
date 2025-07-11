
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.AssistanceAgent;

@Validator
public class AssistanceAgentEmployeCodeValidator extends AbstractValidator<ValidAssistanceAgent, AssistanceAgent> {

	@Override
	protected void initialise(final ValidAssistanceAgent annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AssistanceAgent value, final ConstraintValidatorContext context) {
		if (value.getEmployeCode() == null || value.getEmployeCode().equals("") || value.getEmployeCode().length() < 2)
			super.state(context, true, "employeCode", "{assistance-agent.employeCode.notEmpty}");
		else {
			String name = value.getIdentity().getName();
			String surname = value.getIdentity().getSurname();
			String identifierNumber = value.getEmployeCode();
			// Inicializar las iniciales por si name o surname son null y que no se produzca null pointer exception
			char nameInitial = '-';
			char surnameInitial = '-';
			// Validar nombre
			if (name == null || name.isBlank())
				super.state(context, false, "employeCode", "Name does not exist");
			else {
				name = name.trim().toUpperCase();
				nameInitial = name.charAt(0);
			}

			// Validar apellido
			if (surname == null || surname.isBlank())
				super.state(context, false, "employeCode", "Surname does not exist");
			else {
				surname = surname.trim().toUpperCase();
				surnameInitial = surname.charAt(0);
			}

			identifierNumber = identifierNumber.trim();

			// Comparar iniciales con el identificador
			if (identifierNumber.charAt(0) != nameInitial || identifierNumber.charAt(1) != surnameInitial)
				super.state(context, false, "employeCode", "{assistance-agent.employeCode.validation.letras}");
		}

		return !super.hasErrors(context);
	}

}
