
package acme.entities.crew;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidLongText;
import acme.datatypes.Availability;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CrewMember extends AbstractEntity {

	/*
	 * The flight crew members are the people responsible for operating aircrafts and en-suring passenger safety
	 * and comfort during a flight. The system must store the following data about them: an employee code (unique,
	 * pattern "^[A-Z]{2,3}\d{6}$", where the first two or three letters correspond to their initials), a phone
	 * number (pattern "^+?\d{6,15}$"), their language skills (up to 255 characters), their availability status
	 * ("AVAILABLE", "ON VACATION", "ON LEAVE"), the airline they are working for, and their salary. Optionally,
	 * the system may store his or her years of experience.
	 */

	private static final long	serialVersionUID	= 1L;

	// employee code
	@Mandatory
	@Pattern(regexp = "\\^[A-Z]{2,3}\\d{6}\\$")
	@Automapped
	private String				code;

	// phone number
	@Mandatory
	@NotNull
	@NotBlank
	@Pattern(regexp = "^+?\\d{6,15}$")
	private String				phone;

	// language skills
	@Mandatory
	@Automapped
	@ValidLongText
	private String				languageSkills;

	// availability status
	@Mandatory
	private Availability		availability;

	// salary
	@Mandatory
	private Money				salary;

	// years of experience
	@Optional
	@Min(0)
	private Integer				experience;

}
