
package acme.entities.flight;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Atributos directos
	// -------------------------------------------------------------------

	@NotBlank
	@Size(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				indication;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@Size(max = 255)
	@Automapped
	private String				description;

	// -------------------------------------------------------------------

	// Atributos derivados
	//TODO: Atributos derivados de legs

	// -------------------------------------------------------------------

	// Relaciones 

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager				manager;

	// -------------------------------------------------------------------

}
