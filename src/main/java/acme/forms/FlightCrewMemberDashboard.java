
package acme.forms;

import java.util.List;
import java.util.Map;
import java.util.Set;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.AssignmentStatus;
import acme.entities.flight_assignment.FlightAssignment;
import acme.realms.FlightCrewMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	/**
	 * The system must handle flight crew member dashboards with the following indica-tors:
	 * • The last five destinations to which they have been assigned.
	 * • The number of legs that have an activity log record with an incident severity rang-ing from 0 up to 3, 4 up to 7, and 8 up to 10.
	 * • The crew members who were assigned with him or her in their last leg.
	 * • Their flight assignments grouped by their statuses.
	 * • The average, minimum, maximum, and standard deviation of the number of flight assignments they had in the last month.
	 */

	private static final long						serialVersionUID	= 1L;

	private List<String>							lastDestinations;

	private Integer									minorIncidentLogs; // number of logs with a severity lesser or equal to 3

	private Double									mediumIncidentLogs;

	private Double									mayorIncidentLogs; // number of logs with a severity greater or equal to 8

	private Set<FlightCrewMember>					lastCoworkers;

	private Map<AssignmentStatus, FlightAssignment>	assignmentByStatus;

	private Double									averageAssignments;

	private Double									minimumAssignments;

	private Double									maximumAssignments;

	private Double									standardDeviationAssignments;
}
