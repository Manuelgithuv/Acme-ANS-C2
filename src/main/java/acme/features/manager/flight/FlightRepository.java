package acme.features.manager.flight;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;

@Repository
public interface FlightRepository extends AbstractRepository{
	
	@Query("SELECT f from Flight f WHERE f.manager.id=:managerId")
	List<Flight> findFlightsByManagerId(@Param("managerId") int managerId);
	
	@Query("SELECT f from Flight f WHERE f.id=:flightId")
	Flight findById(@Param("flightId") int flightId);
	
	@Query("SELECT f FROM Flight f WHERE f.published=true")
	List<Flight> findPublicFlights();
	

}
