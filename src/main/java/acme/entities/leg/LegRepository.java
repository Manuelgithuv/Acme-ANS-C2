
package acme.entities.leg;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface LegRepository extends AbstractRepository {

	@Query("SELECT COUNT(l) - 1 FROM Leg l WHERE l.flight.id = :flightId")
	Long countLayoversByFlight(@Param("flightId") int flightId);

	@Query("SELECT COUNT(l) > 0 FROM Leg l WHERE l.flightCode = :flightCode")
	boolean existsByFlightCode(@Param("flightCode") String flightCode);

	@Query("SELECT MIN(l.scheduledDeparture) FROM Leg l WHERE l.flight.id = :flightId")
	Optional<Date> findFirstScheduledDeparture(@Param("flightId") int flightId);

	@Query("SELECT MAX(l.scheduledArrival) FROM Leg l WHERE l.flight.id = :flightId")
	Optional<Date> findLastScheduledArrival(@Param("flightId") int flightId);

	@Query("""
		    SELECT l.departureAirport.city
		    FROM Leg l
		    WHERE l.flight.id = :flightId
		    AND l.scheduledDeparture = (SELECT MIN(l2.scheduledDeparture) FROM Leg l2 WHERE l2.flight.id = :flightId)
		""")
	Optional<String> findOriginCity(@Param("flightId") int flightId);

	@Query("""
		    SELECT l.arrivalAirport.city
		    FROM Leg l
		    WHERE l.flight.id = :flightId
		    AND l.scheduledArrival = (SELECT MAX(l2.scheduledArrival) FROM Leg l2 WHERE l2.flight.id = :flightId)
		""")
	Optional<String> findDestinationCity(@Param("flightId") int flightId);

}
