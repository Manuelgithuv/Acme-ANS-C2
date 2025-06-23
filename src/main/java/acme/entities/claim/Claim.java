
package acme.entities.claim;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidLongText;
import acme.datatypes.ClaimStatus;
import acme.datatypes.ClaimType;
import acme.entities.claimLog.ClaimTrackingLog;
import acme.entities.leg.Leg;
import acme.features.assistanceAgents.trackingLog.AssistanceAgentTrackingLogRepository;
import acme.realms.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "claim", indexes = {
	@Index(name = "idx_claim_agent", columnList = "assistance_agent_id"), @Index(name = "idx_claim_leg", columnList = "leg_id"), @Index(name = "idx_claim_email", columnList = "passengerEmail")
})
public class Claim extends AbstractEntity {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@Mandatory
	@ValidEmail
	@Automapped
	private String				passengerEmail;

	@Mandatory
	@ValidLongText
	@Automapped
	private String				description;

	@Mandatory
	@Valid
	@Automapped
	private ClaimType			type;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				published;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent		assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg					Leg;


	@Transient
	public ClaimTrackingLog getLastTrackingLog() {
		ClaimTrackingLog result = null;
		AssistanceAgentTrackingLogRepository repository;
		repository = SpringHelper.getBean(AssistanceAgentTrackingLogRepository.class);
		List<ClaimTrackingLog> ls = repository.findAllByClaimIdOrderByCreationMomentDescIdDesc(this.getId());
		if (!ls.isEmpty())
			result = ls.getFirst();
		return result;
	}

	@Transient
	public ClaimStatus getStatus() {
		ClaimStatus result;
		ClaimTrackingLog lastLog = this.getLastTrackingLog();
		if (lastLog == null)
			result = ClaimStatus.PENDING;
		else
			result = lastLog.getStatus();

		return result;
	}

	@Transient
	public Boolean getIsAccepted() {

		return this.getStatus().equals(ClaimStatus.ACCEPTED);
	}

}
