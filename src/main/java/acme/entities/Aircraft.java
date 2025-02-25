
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Size(max = 50)
	@Automapped
	private String				model;

	@Mandatory
	@Size(max = 50)
	@Column(unique = true)
	private String				registrationNumber;

	@Mandatory
	@Valid
	@Automapped
	private Integer				capacity;

	@Mandatory
	@DecimalMin(value = "2000.0", message = "Cargo weight must be at least 2,000")
	@DecimalMax(value = "50000.0", message = "Cargo weight must be at most 50,000")
	@Automapped
	private double				cargoWeight;

	@Mandatory
	@Automapped
	private boolean				status;

	@Optional
	@Size(max = 255)
	@Automapped
	private String				details;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
