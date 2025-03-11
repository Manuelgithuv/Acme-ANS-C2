
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.constraints.ValidLicenseNumber;
import acme.constraints.ValidLongText;
import acme.constraints.ValidPhoneNumber;
import acme.constraints.ValidSpecialisation;
import acme.constraints.ValidYearsOfExperience;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Technician extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidLicenseNumber         //
	@Column(unique = true)
	private String				licenseNumber;

	@Mandatory
	@ValidPhoneNumber           //
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidSpecialisation       //
	@Automapped
	private String				specialisation;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				annualHealthTestPassed;

	@Mandatory
	@ValidYearsOfExperience     //
	@Automapped
	private Integer				yearsOfExperience;

	@Optional
	@ValidLongText
	@Automapped
	private String				certifications;

}
