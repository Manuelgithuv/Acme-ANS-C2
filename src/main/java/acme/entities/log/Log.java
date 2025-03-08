
package acme.entities.log;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidLog;
import acme.entities.crew.FlightAssignment;
import acme.entities.leg.Leg;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@ValidLog
public class Log extends AbstractEntity {

	/*
	 * An activity log records incidents that occur during a flight. They are logged by any of the flight crew
	 * members assigned to the corresponding leg and after the leg has taken place. The incidents include
	 * weather-related disruptions, route deviations, passenger issues, or mechanical failures, to mention a few.
	 * Each log entry includes a registration moment (in the past), a type of incident (up to 50 characters) a
	 * description (up to 255 characters), and a severity level (ranging from 0 to 10, where 0 indicates no issue
	 * and 10 represents a highly critical situation).
	 */

	private static final long	serialVersionUID	= 1L;

	// crew member
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private FlightAssignment	flightAssignment;

	// leg
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					leg;

	// registration moment
	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	// type of incident
	@Mandatory
	@Valid
	@Automapped
	@Size(max = 50)
	private String				incidentType;

	// description
	@Mandatory
	@Valid
	@Automapped
	@Size(max = 255)
	private String				description;

	// severity level
	@Mandatory
	@Valid
	@Automapped
	@Min(0)
	@Max(10)
	private Integer				severity;

}
