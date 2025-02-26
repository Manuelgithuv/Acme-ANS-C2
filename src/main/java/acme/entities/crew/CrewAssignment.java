
package acme.entities.crew;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.datatypes.AssignmentStatus;
import acme.datatypes.CrewDuty;
import acme.entities.Leg;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CrewAssignment extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// crew member
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private CrewMember			asignee;

	// leg
	@Mandatory
	@Valid
	@OneToOne(optional = false)
	private Leg					leg;

	// duty
	@Mandatory
	@Automapped
	@Valid
	private CrewDuty			duty;

	// last update
	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdate;

	// current status
	@Mandatory
	@Automapped
	@Valid
	private AssignmentStatus	status;

	// remarks
	@Optional
	@Size(max = 255)
	@Automapped
	private String				remarks;

}
