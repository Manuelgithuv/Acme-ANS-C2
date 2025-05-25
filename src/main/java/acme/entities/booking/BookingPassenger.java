
package acme.entities.booking;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.entities.passenger.Passenger;
import acme.realms.Customer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "booking_passenger", indexes = {
	@Index(name = "idx_bp_customer_id", columnList = "customer_id"), @Index(name = "idx_bp_booking_id", columnList = "booking_id"), @Index(name = "idx_bp_passenger_booking", columnList = "passenger_id, booking_id"),
	@Index(name = "idx_bp_published", columnList = "published")
})
public class BookingPassenger extends AbstractEntity {
	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	private boolean				published;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Booking				booking;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Passenger			passenger;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

}
