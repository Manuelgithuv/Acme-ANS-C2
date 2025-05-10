
package acme.components;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.realms.FlightCrew;

@Repository
public interface FlightCrewRepository extends AbstractRepository {

	@Query("select fc from FlightCrew fc where fc.airline.id = :airlineId")
	public Collection<FlightCrew> findAllByAirline(@Param("airlineId") int airlineId);

}
