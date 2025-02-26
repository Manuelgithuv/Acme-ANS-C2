
package acme.entities.crew;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.datatypes.Availability;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Crew {

	// employee code
	@Mandatory
	@Pattern(regexp = "\\^[A-Z]{2,3}\\d{6}\\$")
	@Automapped
	private String code;

	// phone number
	@Mandatory
	@Pattern(regexp = "\\^+?\\d{6,15}\\$")
	private String phone;

	// language skills
	@Mandatory
	@Size(max = 255)
	@Automapped
	private String languageSkills;

	// availability status
	@Mandatory
	private Availability availability;

	// salary
	@Mandatory
	private Money salary;

	// years of experience
	@Optional
	@Min(0)
	private Integer experience;

}
