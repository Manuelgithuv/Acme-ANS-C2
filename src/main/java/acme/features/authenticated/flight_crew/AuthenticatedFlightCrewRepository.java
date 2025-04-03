
package acme.features.authenticated.flight_crew;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.FlightCrew;

@Repository
public interface AuthenticatedFlightCrewRepository extends AbstractRepository {

	@Query("select fc from FlightCrew fc where fc.userAccount.id = :id")
	FlightCrew findOneFlightCrewByUserAccountId(int id);

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("select fc from FlightCrew fc where fc.identifier=:identifier")
	Optional<FlightCrew> findByIdentifier(@Param("identifier") String identifier);
}
