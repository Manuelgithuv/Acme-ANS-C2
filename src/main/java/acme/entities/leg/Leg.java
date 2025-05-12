
package acme.entities.leg;

import java.util.Date;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidFlightCode;
import acme.constraints.ValidHours;
import acme.datatypes.LegStatus;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.realms.Manager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ValidHours
@ValidFlightCode
@Entity
@Table(
	  name = "leg",
	  indexes = {
	    @Index(name = "idx_leg_flight_dep", columnList = "flight_id, scheduledDeparture"),
	    @Index(name = "idx_leg_flight_arr", columnList = "flight_id, scheduledArrival"),
	    @Index(name = "idx_leg_aircraft_pub_dep", columnList = "aircraft_id, published, scheduledDeparture"),
	    @Index(name = "idx_leg_flight_code", columnList = "flightCode")
	  }
	)
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Atributos directos

	// -------------------------------------------------------------------
	@Mandatory
	@Valid
	@Column(unique = true)
	private String				flightCode;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	@Mandatory
	@Valid
	@Automapped
	private Double				hours;

	@Mandatory
	@Automapped
	private boolean				published;

	// -------------------------------------------------------------------
	// Relaciones 

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport				arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft			aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Manager				manager;

}
