
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.constraints.ValidExperience;
import acme.constraints.ValidFlightCrewMemberIdentifier;
import acme.constraints.ValidIdentifier;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPhone;
import acme.datatypes.Availability;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@ValidFlightCrewMemberIdentifier
public class FlightCrewMember extends AbstractRole {

	/*
	 * The flight crew members are the people responsible for operating aircrafts and en-suring passenger safety
	 * and comfort during a flight. The system must store the following data about them: an employee code (unique,
	 * pattern "^[A-Z]{2,3}\d{6}$", where the first two or three letters correspond to their initials), a phone
	 * number (pattern "^+?\d{6,15}$"), their language skills (up to 255 characters), their availability status
	 * ("AVAILABLE", "ON VACATION", "ON LEAVE"), the airline they are working for, and their salary. Optionally,
	 * the system may store his or her years of experience.
	 */

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidIdentifier
	@Column(unique = true)
	private String				identifier;

	@Mandatory
	@ValidPhone
	@Automapped
	private String				phone;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				languageSkills;

	@Mandatory
	@Valid
	@Automapped
	private Availability		availability;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@ValidExperience
	@Automapped
	private Integer				experience;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Automapped
	@ManyToOne(optional = false)
	private Airline				airline;

}
