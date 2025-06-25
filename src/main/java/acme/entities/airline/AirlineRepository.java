
package acme.entities.airline;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirlineRepository extends AbstractRepository {

	@Query("SELECT COUNT(a) FROM Airline a WHERE a.iataCode = :code")
	long countByAirlineCode(@Param("code") String code);

	@Query("SELECT a FROM Airline a WHERE a.id = :id")
	Airline findById(@Param("id") int id);

	@Query("SELECT a FROM Airline a")
	List<Airline> findAllAirlines();

}
