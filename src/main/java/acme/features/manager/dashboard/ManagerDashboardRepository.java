package acme.features.manager.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.Manager;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {
	
	@Query("SELECT m FROM Manager m ORDER BY m.yearsOfExperience DESC")
    List<Manager> findAllOrderedByExperience();
	
	@Query("SELECT CASE WHEN (65 - (YEAR(CURRENT_DATE) - YEAR(m.dateOfBirth))) > 0 THEN (65 - (YEAR(CURRENT_DATE) - YEAR(m.dateOfBirth))) ELSE 0 END FROM Manager m WHERE m.id = :managerId")
	Integer getYearsToRetirement(@Param("managerId") int managerId);
	
	@Query("SELECT AVG(CASE WHEN l.status = 'ON_TIME' THEN 1 ELSE 0 END) FROM Leg l WHERE l.manager.id = :managerId")
	double getOnTimeRatio(@Param("managerId") int managerId);

	@Query("SELECT AVG(CASE WHEN l.status = 'DELAYED' THEN 1 ELSE 0 END) FROM Leg l WHERE l.manager.id = :managerId")
	double getDelayedRatio(@Param("managerId") int managerId);
	
	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId ORDER BY l.scheduledDeparture DESC")
	List<Leg> findLegsByFlightId(@Param("flightId") int flightId);
	
	@Query("SELECT f FROM Flight f WHERE f.manager.id = :managerId")
	List<Flight> findFlightsByManagerId(@Param("managerId") int managerId);




}
