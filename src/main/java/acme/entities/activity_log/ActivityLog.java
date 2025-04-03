
package acme.entities.activity_log;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidActivityLog;
import acme.constraints.ValidIncidentType;
import acme.constraints.ValidLongText;
import acme.constraints.ValidSeverity;
import acme.entities.flight_assignment.FlightAssignment;
import acme.entities.leg.Leg;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@ValidActivityLog
public class ActivityLog extends AbstractEntity {

	/*
	 * An activity log records incidents that occur during a flight. They are logged by any of the flight crew
	 * members assigned to the corresponding leg and after the leg has taken place. The incidents include
	 * weather-related disruptions, route deviations, passenger issues, or mechanical failures, to mention a few.
	 * Each log entry includes a registration moment (in the past), a type of incident (up to 50 characters) a
	 * description (up to 255 characters), and a severity level (ranging from 0 to 10, where 0 indicates no issue
	 * and 10 represents a highly critical situation).
	 */

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidIncidentType
	@Automapped
	private String				incidentType;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				description;

	@Mandatory
	@ValidSeverity
	@Automapped
	private Integer				severity;

	@Mandatory
	@Automapped
	private Boolean				published;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightAssignment	flightAssignment;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;

}
