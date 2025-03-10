
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
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
public class Customer extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Pattern(regexp = "^[A-Z]{2,3}\\d{6}$", message = "El identificador debe tener 2-3 letras mayúsculas seguidas de 6 dígitos")
	@Column(unique = true)
	private String				identifier;

	@Mandatory
	@NotBlank
	@Pattern(regexp = "^\\+?\\d{6,15}$", message = "El número de teléfono debe tener entre 6 y 15 dígitos")
	private String				phoneNumber;

	@Mandatory
	@NotBlank
	@Size(max = 255)
	@Automapped
	private String				physicalAddress;

	@Mandatory
	@NotBlank
	@Size(max = 50)
	@Automapped
	private String				city;

	@Mandatory
	@NotBlank
	@Size(max = 50)
	@Automapped
	private String				country;

	@Optional
	@PositiveOrZero
	@Automapped
	private Integer				earnedPoints;
}
