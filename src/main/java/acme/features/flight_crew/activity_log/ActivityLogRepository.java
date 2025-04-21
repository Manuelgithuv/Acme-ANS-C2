
package acme.features.flight_crew.activity_log;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activity_log.ActivityLog;

@Repository
public interface ActivityLogRepository extends AbstractRepository {

	@Query("select a from ActivityLog a where a.id = :activityLogId")
	ActivityLog findById(@Param("activityLogId") int activityLogId);

	@Query("select a from ActivityLog a where a.flightAssignment.assignee.id = :flightCrewId")
	Collection<ActivityLog> findByFlightCrewId(@Param("flightCrewId") int flightCrewId);

}
