
package acme.entities.airport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidUrl;
import acme.datatypes.OperationalScope;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Airport extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Size(max = 50)
	@Automapped
	private String				name;

	@NotBlank
	@Pattern(regexp = "^[A-Z]{3}$", message = "IATA code must be a 3-letter uppercase code")
	@Column(unique = true)
	private String				iataCode;

	@Mandatory
	@Valid
	@Automapped
	private OperationalScope	scope;

	@NotBlank
	@Size(max = 50)
	@Automapped
	private String				city;

	@NotBlank
	@Size(max = 50)
	@Automapped
	private String				country;

	@Optional
	@ValidUrl
	@Automapped
	private String				webSite;

	@Optional
	@ValidEmail
	@Automapped
	private String				email;

	@Optional
	@Pattern(regexp = "^\\+?\\d{6,15}$", message = "Phone number must match the pattern ^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

}
