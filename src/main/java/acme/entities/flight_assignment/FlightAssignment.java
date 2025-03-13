
package acme.entities.flight_assignment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidLongText;
import acme.datatypes.AssignmentStatus;
import acme.datatypes.CrewDuty;
import acme.realms.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FlightAssignment extends AbstractEntity {

	/*
	 * A flight assignment represents the allocation of a flight crew member to a specific leg of a flight.
	 * Each assignment specifies the flight crew's duty in that leg ("PILOT", "CO-PILOT", "LEAD ATTENDANT",
	 * "CABIN ATTENDANT"), the moment of the last up-date (in the past), the current status of the assignment
	 * ("CONFIRMED", "PENDING", or "CANCELLED"), and some remarks (up to 255 characters), if necessary.
	 */

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@Valid
	@Automapped
	private CrewDuty			duty;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdate;

	@Mandatory
	@Valid
	@Automapped
	private AssignmentStatus	status;

	@Optional
	@ValidLongText
	@Automapped
	private String				remarks;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightCrewMember	asignee;

}
