
package acme.entities.task;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.constraints.ValidDescription;
import acme.datatypes.TaskType;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Task extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	@Automapped
	private TaskType			type;

	@Mandatory
	@ValidDescription
	@Automapped
	private String				description;

	@Mandatory
	@ValidNumber(min = 0, max = 10, integer = 2)
	@Automapped
	private int					priority;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician			technician;

}
