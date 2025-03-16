package acme.entities.airport;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AirportRepository extends AbstractRepository{
	
	//TODO: Mover a features cuando se empieza con la funcionalidad de airports
	
	@Query("SELECT a FROM Airport a")
	Collection<Airport> findAllAirports();

}
