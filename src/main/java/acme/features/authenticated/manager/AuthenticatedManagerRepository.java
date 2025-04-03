
package acme.features.authenticated.manager;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.Manager;

@Repository
public interface AuthenticatedManagerRepository extends AbstractRepository {

	@Query("select m from Manager m where m.userAccount.id = :id")
	Manager findOneManagerByUserAccountId(int id);

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("select m from Manager m where m.identifierNumber=:identifierNumber")
	Optional<Manager> findByIdentifierNumber(@Param("identifierNumber") String identifierNumber);

}
