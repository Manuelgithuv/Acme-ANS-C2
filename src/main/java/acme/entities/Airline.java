
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidUrl;
import acme.datatypes.AirlineType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Airline extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Size(max = 50)
	@Automapped
	private String				name;

	@NotBlank
	@Pattern(regexp = "^[A-Z]{2}[A-Z0-9]X$", message = "IATA code must be a 3-letter uppercase code ending in 'X'")
	@Column(unique = true)
	private String				iataCode;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				webSite;

	@Mandatory
	@Valid
	@Automapped
	private AirlineType			type;

	@Mandatory
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				foundationMoment;

	@Optional
	@ValidEmail
	@Automapped
	private String				email;

	@Mandatory
	@Pattern(regexp = "^\\+?\\d{6,15}$", message = "Phone number must match the pattern ^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

}
