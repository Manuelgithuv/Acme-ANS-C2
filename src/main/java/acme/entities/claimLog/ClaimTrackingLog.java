
package acme.entities.claimLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidScore;
import acme.constraints.ValidClaimTrackingLog;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import acme.datatypes.ClaimLogStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ValidClaimTrackingLog
@Entity
public class ClaimTrackingLog extends AbstractEntity {

	/**
	 * lastUpdateMoment: Date {Mandatory, ValidMoment(past=true), Temporal(TemporalType.TIMESTAMP)}
	 * + stepUndergoing: String {Mandatory, ValidShortText, Automapped}
	 * + resolutionPercentage: Double {Mandatory, ValidScore, Automapped}
	 * + status: claimLogStatus {Mandatory, ValidScore, Automapped}
	 * + resolutionDescription: String {Optional, ValidLongText, Automapped}
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdateMoment;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				stepUndergoing;

	@Mandatory
	@ValidScore
	@Automapped
	private Double				resolutionPercentage;

	@Mandatory
	@Valid
	@Automapped
	private ClaimLogStatus		status;

	@Optional
	@ValidLongText
	@Automapped
	private String				resolutionDescription;

}
