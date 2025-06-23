
package acme.entities.claimLog;

import java.beans.Transient;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidScore;
import acme.constraints.ValidClaimTrackingLog;
import acme.constraints.ValidLongText;
import acme.constraints.ValidShortText;
import acme.datatypes.ClaimStatus;
import acme.entities.claim.Claim;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ValidClaimTrackingLog
@Entity
@Table(name = "claim_tracking_log", indexes = {
	@Index(name = "idx_log_claim", columnList = "claim_id"), @Index(name = "idx_log_status", columnList = "status"), @Index(name = "idx_log_lastupdate", columnList = "lastUpdateMoment"),
	@Index(name = "idx_log_creationMoment", columnList = "creationMoment"), @Index(name = "idx_log_claim_agent", columnList = "claim_id, published")
})
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
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				creationMoment;

	@Mandatory
	@ValidShortText
	@Automapped
	private String				stepUndergoing;

	@Mandatory
	@ValidScore
	@Automapped
	private Double				resolutionPercentage;

	@Optional
	@ValidLongText
	@Automapped
	private String				resolutionDescription;

	@Optional
	@ValidMoney
	@Automapped
	private Money				compensation;

	@Mandatory
	@Valid
	@Automapped
	private ClaimStatus			status;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				published;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Claim				claim;


	@Transient
	public Boolean getIsAcepted() {

		return this.getClaim().getIsAccepted();
	}

	@Transient
	public ClaimStatus getStatus() {

		return this.status;
	}

}
