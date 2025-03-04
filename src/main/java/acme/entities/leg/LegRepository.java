
package acme.entities.leg;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("SELECT COUNT(l) - 1 FROM Leg l WHERE l.flight.id = :flightId")
	int countLayoversByFlight(@Param("flightId") Long flightId);

	@Query("SELECT COUNT(l) > 0 FROM Leg l WHERE l.flightCode = :flightCode")
	boolean existsByFlightCode(@Param("flightCode") String flightCode);

}
