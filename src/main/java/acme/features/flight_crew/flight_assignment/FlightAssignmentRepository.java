
package acme.features.flight_crew.flight_assignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight_assignment.FlightAssignment;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightAssignment f where f.assignee.userAccount.id = :flightCrewMemberId")
	Collection<FlightAssignment> findFlightAssignmentByAssigneeId(@Param("flightCrewMemberId") int flightCrewMemberId);

	@Query("select f from FlightAssignment f where leg.id = :legId")
	Collection<FlightAssignment> findByLegId(@Param("legId") int legId);

	@Query("select f from FlightAssignment f where f.assignee.userAccount.id = :flightCrewMemberId and f.leg.id = :legId")
	FlightAssignment findByAssigneeAndLeg(@Param("flightCrewMemberId") int flightCrewMemberId, @Param("legId") int legId);

	@Query("select f from FlightAssignment f where f.id = :flightAssignmentId")
	FlightAssignment findById(@Param("flightAssignmentId") int flightAssignmentId);

	@Query("select f from FlightAssignment f where f.published = true")
	Collection<FlightAssignment> findPublicFlightAssignments();

}
