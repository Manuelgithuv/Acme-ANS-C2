package acme.entities.aircraft;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface AircraftRepository extends AbstractRepository{
	
	//TODO: Mover el repo a features cuando se empiece con la funcionalidad de aircrafts
	
	@Query("SELECT a FROM Aircraft a")
	Collection<Aircraft> findAllAircrafts();

}
