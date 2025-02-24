
package acme.features.airline;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirlineRepository extends AbstractRepository {

	@Query("SELECT COUNT(a) FROM Airline a WHERE a.iataCode = :code")
	long countByAirlineCode(@Param("code") String code);

}
