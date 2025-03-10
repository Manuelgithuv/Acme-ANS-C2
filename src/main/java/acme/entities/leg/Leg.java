
package acme.entities.leg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidMoment;
import acme.constraints.ValidFlightCode;
import acme.constraints.ValidTimeBetweenConsecutiveLegs;
import acme.datatypes.LegStatus;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ValidFlightCode
@ValidTimeBetweenConsecutiveLegs
@Entity
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Atributos directos

	// -------------------------------------------------------------------
	@NotBlank
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

	// -------------------------------------------------------------------
	// Derivados


	@Transient
	public Double getHours() {
		if (this.scheduledDeparture == null || this.scheduledArrival == null)
			return null;
		long diffInMillis = this.scheduledArrival.getTime() - this.scheduledDeparture.getTime();
		return diffInMillis / (1000.0 * 60 * 60);
	}

	// ----------------------------------------------

	// -------------------------------------------------------------------
	// Relaciones 


	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		departureAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airport		arrivalAirport;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft	aircraft;

}
