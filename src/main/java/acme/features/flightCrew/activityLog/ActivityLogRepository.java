
package acme.features.flightCrew.activityLog;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;

@Repository
public interface ActivityLogRepository extends AbstractRepository {

	@Query("select a from ActivityLog a where a.id = :activityLogId")
	ActivityLog findById(@Param("activityLogId") int activityLogId);

	@Query("select a from ActivityLog a where a.flightAssignment.crewMember.id = : flightCrewId")
	List<ActivityLog> findByFlightCrewId(@Param("flightCrewId") int flightCrewId);

}
