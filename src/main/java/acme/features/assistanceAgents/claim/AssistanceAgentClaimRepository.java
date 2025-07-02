
package acme.features.assistanceAgents.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :assistanceAgentId")
	Collection<Claim> findAllByAssistanceAgentId(int assistanceAgentId);

	@Query("SELECT c FROM Claim c WHERE c.id = :id")
	Claim findClaimById(@Param("id") int id);

	@Query("SELECT c FROM Claim c WHERE c.published = true")
	Collection<Claim> findAllPublishedClaims();

	@Query("SELECT a FROM AssistanceAgent a")
	Collection<AssistanceAgent> findAllAssistanceAgents();

}
