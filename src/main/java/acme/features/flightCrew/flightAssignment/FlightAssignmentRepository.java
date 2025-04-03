
package acme.features.flightCrew.flightAssignment;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight_assignment.FlightAssignment;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("select f from FlightAssignment f where f.assignee.id = :flightCrewMemberId")
	List<FlightAssignment> findFlightAssignmentByAssigneeId(@Param("flightCrewMemberId") int flightCrewMemberId);

	@Query("select f from FlightAssignment f where leg.id = :legId")
	List<FlightAssignment> findByLegId(@Param("legId") int legId);

	@Query("select f from FlightAssignment f where f.assignee.id = :flightCrewMemberId and f.leg.id = :legId")
	FlightAssignment findByAssigneeAndLeg(@Param("flightCrewMemberId") int flightCrewMemberId, @Param("legId") int legId);

	@Query("select f from FlightAssignment f where f.id = :flightAssignmentId")
	FlightAssignment findById(@Param("flightAssignmentId") int flightAssignmentId);

	@Query("select f from FlightAssignment f where f.published = true")
	List<FlightAssignment> findPublicFlightAssignments();

}
