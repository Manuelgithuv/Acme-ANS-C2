package acme.entities.airport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirportRepository extends AbstractRepository{
	
	//TODO: Mover a features cuando se empieza con la funcionalidad de airports
	
	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();
	
	@Query("SELECT a FROM Airport a WHERE a.id=:airportId")
	Airport findById(@Param("airportId") int airportId);

}
