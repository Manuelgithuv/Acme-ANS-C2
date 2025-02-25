
package acme.entities;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
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


	@Transient
	public long getNumberOfLayovers() {
		return 0;
	}

	// -------------------------------------------------------------------

	// Relaciones 

	// -------------------------------------------------------------------

}
