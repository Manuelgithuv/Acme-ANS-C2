
package acme.features.assistanceAgents.trackingLog;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claimLog.ClaimTrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("SELECT log FROM ClaimTrackingLog log WHERE log.claim.id = :claimId ORDER BY log.creationMoment DESC, log.id DESC")
	List<ClaimTrackingLog> findAllByClaimIdOrderByCreationMomentDescIdDesc(@Param("claimId") int claimId);

	@Query("SELECT log FROM ClaimTrackingLog log WHERE log.claim.assistanceAgent.id = :assistanceAgentId")
	Collection<ClaimTrackingLog> findAllClaimTrackingLogByAssistanceAgentId(@Param("assistanceAgentId") int assistanceAgentId);

	@Query("SELECT c FROM ClaimTrackingLog c WHERE c.id = :id")
	ClaimTrackingLog findClaimTrackingLogById(@Param("id") int id);

}
