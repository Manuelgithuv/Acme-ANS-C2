
package acme.realms;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.entities.booking.Booking;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Passenger extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@NotBlank
	@Size(max = 255)
	@Automapped
	private String				fullName;

	@Optional
	@ValidEmail
	@Automapped
	private String				email;

	@Mandatory
	@NotBlank
	@Pattern(regexp = "^[A-Z0-9]{6,9}$", message = "El número de pasaporte debe tener entre 6 y 9 caracteres alfanuméricos en mayúscula")
	private String				passportNumber;

	@Mandatory
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateOfBirth;

	@Optional
	@Size(max = 50)
	private String				specialNeeds;

	// Relaciones
	// -------------------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Booking				booking;
}
