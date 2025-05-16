
package acme.features.flight_crew.flight_assignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight_assignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.FlightCrew;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.assignee.id = :flightCrewMemberId")
	Collection<FlightAssignment> findFlightAssignmentByAssigneeId(@Param("flightCrewMemberId") int flightCrewMemberId);

	@Query("select fa from FlightAssignment fa")
	Collection<FlightAssignment> findAllFlightAssignment();

	@Query("select fa from FlightAssignment fa where leg.id = :legId")
	Collection<FlightAssignment> findByLegId(@Param("legId") int legId);

	@Query("select fa from FlightAssignment fa where fa.assignee = :flightCrewMember and fa.leg.id = :legId")
	FlightAssignment findByAssigneeAndLeg(@Param("flightCrewMember") FlightCrew flightCrewMember, @Param("legId") int legId);

	@Query("select fa from FlightAssignment fa where fa.id = :flightAssignmentId")
	FlightAssignment findById(@Param("flightAssignmentId") int flightAssignmentId);

	@Query("select fa from FlightAssignment fa where fa.published = true")
	Collection<FlightAssignment> findPublicFlightAssignments();

	@Query("select fa.leg from FlightAssignment fa where fa.assignee.userAccount.id = :flightCrewMemberId")
	Collection<Leg> findLegsByCrew(@Param("flightCrewMemberId") int flightCrewMemberId);

	@Query("select fa.assignee from FlightAssignment fa where fa.leg.id = :legId and fa.duty = 'LEAD_ATTENDANT'")
	FlightCrew findLeadAttendantByLeg(@Param("legId") int legId);

}
