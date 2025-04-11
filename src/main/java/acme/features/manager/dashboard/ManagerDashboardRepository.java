
package acme.features.manager.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.system_configuration.SystemConfiguration;
import acme.realms.Manager;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select m from Manager m where m.id = :id")
	Manager findManagerById(int id);

	@Query("SELECT m FROM Manager m ORDER BY m.yearsOfExperience DESC")
	List<Manager> findAllOrderedByExperience();

	@Query("select count(l) from Leg l where l.flight.manager.id = :id and l.status = acme.datatypes.LegStatus.ON_TIME AND l.flight.published=true")
	int countOnTimeLegs(int id);

	@Query("select count(l) from Leg l where l.flight.manager.id = :id and l.status = acme.datatypes.LegStatus.DELAYED AND l.flight.published=true")
	int countDelayedLegs(int id);

	@Query("select count(l) from Leg l where l.flight.manager.id = :id and l.status = acme.datatypes.LegStatus.CANCELLED AND l.flight.published=true")
	int countCancelledLegs(int id);

	@Query("select count(l) from Leg l where l.flight.manager.id = :id and l.status = acme.datatypes.LegStatus.LANDED AND l.flight.published=true ")
	int countLandedLegs(int id);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId AND l.flight.published = true ORDER BY l.scheduledDeparture ASC")
	List<Leg> findLegsByFlightId(@Param("flightId") int flightId);

	@Query("SELECT f FROM Flight f WHERE f.manager.id = :managerId AND f.published=true")
	List<Flight> findFlightsByManagerId(@Param("managerId") int managerId);

	@Query("select avg(f.cost.amount), min(f.cost.amount), max(f.cost.amount), stddev(f.cost.amount) from Flight f where f.manager.id = :id AND f.published=true")
	List<Object[]> statsCost(int id);

	@Query("select sc from SystemConfiguration sc")
	SystemConfiguration getSystemConfiguration();

}
