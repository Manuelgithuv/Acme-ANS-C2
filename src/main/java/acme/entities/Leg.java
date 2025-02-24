
package acme.entities;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.constraints.ValidFlightCode;
import acme.datatypes.LegStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	// Atributos directos

	// -------------------------------------------------------------------
	@Mandatory
	@ValidFlightCode
	@Column(unique = true)
	private String				flightCode;

	@Mandatory
	@Valid
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@Valid
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	// -------------------------------------------------------------------

	// Atributos derivados

	// -------------------------------------------------------------------


	@Transient
	public long getDurationInHours() {
		if (this.scheduledDeparture != null && this.scheduledArrival != null) {
			long diffInMillis = this.scheduledArrival.getTime() - this.scheduledDeparture.getTime();
			return TimeUnit.MILLISECONDS.toHours(diffInMillis);
		}
		return 0;
	}
	// -------------------------------------------------------------------

	// Relaciones 

	// -------------------------------------------------------------------


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
