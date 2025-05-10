
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

	@Query("select f from FlightAssignment f where f.assignee.id = :flightCrewMemberId")
	Collection<FlightAssignment> findFlightAssignmentByAssigneeId(@Param("flightCrewMemberId") int flightCrewMemberId);

	@Query("select f from FlightAssignment f")
	Collection<FlightAssignment> findAllFlightAssignment();

	@Query("select f from FlightAssignment f where leg.id = :legId")
	Collection<FlightAssignment> findByLegId(@Param("legId") int legId);

	@Query("select f from FlightAssignment f where f.assignee = :flightCrewMember and f.leg.id = :legId")
	FlightAssignment findByAssigneeAndLeg(@Param("flightCrewMember") FlightCrew flightCrewMember, @Param("legId") int legId);

	@Query("select f from FlightAssignment f where f.id = :flightAssignmentId")
	FlightAssignment findById(@Param("flightAssignmentId") int flightAssignmentId);

	@Query("select f from FlightAssignment f where f.published = true")
	Collection<FlightAssignment> findPublicFlightAssignments();

	@Query("select f.leg from FlightAssignment f where f.assignee.userAccount.id = :flightCrewMemberId")
	Collection<Leg> findLegsByCrew(@Param("flightCrewMemberId") int flightCrewMemberId);

	//@Query("select f.leg from FlightAssignment f where f.assignee.userAccount.id = :flightCrewMemberId and f.duty = 'LEAD_ATTENDANT'")
	//Collection<Leg> findLegsAsLeadAttendant(@Param("flightCrewMemberId") int flightCrewMemberId);

	//@Query("select f from FlightAssignment f where f.leg in legsAsLeadAttendant and f.duty != 'LEAD_ATTENDANT'")
	//Collection<FlightAssignment> findAssignmentsAsLeadAttendant(@Param("legsAsLeadAttendant") Collection<Leg> legsAsLeadAttendant);

}
