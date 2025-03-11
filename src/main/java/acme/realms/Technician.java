
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Technician extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$", message = "El número de licencia debe tener 2-3 letras mayúsculas seguidas de 6 dígitos")
	@Column(unique = true)
	private String				licenseNumber;

	@Mandatory
	@Pattern(regexp = "^\\+?\\d{6,15}$", message = "El número de teléfono debe tener entre 6 y 15 dígitos")
	private String				phoneNumber;

	@Mandatory
	@Size(max = 50)
	@Automapped
	private String				specialisation;

	@Mandatory
	@Automapped
	private boolean				annualHealthTestPassed;

	@Mandatory
	@PositiveOrZero
	@Automapped
	private Integer				yearsOfExperience;

	@Optional
	@Size(max = 255)
	@Automapped
	private String				certifications;

}
