
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.entities.flight.Flight;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Booking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Pattern(regexp = "^[A-Z0-9]{6,8}$", message = "El código localizador debe tener entre 6 y 8 caracteres alfanuméricos en mayúscula")
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@NotBlank
	@Pattern(regexp = "^(ECONOMY|BUSINESS)$", message = "La clase de viaje debe ser ECONOMY o BUSINESS")
	private String				travelClass;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				price;

	@Optional
	@Pattern(regexp = "^\\d{4}$", message = "Los últimos 4 dígitos de la tarjeta deben ser un número de 4 dígitos")
	@Automapped
	private String				lastCardNibble;

	// Relaciones 
	// -------------------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

	@Mandatory
	@Valid
	@OneToOne(optional = false)
	private Flight				flight;

}
