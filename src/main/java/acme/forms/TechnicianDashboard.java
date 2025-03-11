
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.datatypes.MaintenanceRecordStatus;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianDashboard extends AbstractForm {

	private static final long								serialVersionUID	= 1L;

	private Map<MaintenanceRecord, MaintenanceRecordStatus>	maintenanceRecordByStatus;

	private MaintenanceRecord								maintenanceRecordNearestInspection;

	private Map<Aircraft, Task>								aircraftsWithHigherNumberOfTasks;

	private Double											averageCostDesviationOfMaintenanceRecord;

	private Double											minimumCostDesviationOfMaintenanceRecord;

	private Double											maximumCostDesviationOfMaintenanceRecord;

	private Double											standardCostDesviationOfMaintenanceRecord;

	private Double											averageDurationDesviationOfTasks;

	private Double											minimumDurationDesviationOfTasks;

	private Double											maximumDurationDesviationOfTasks;

	private Double											standardDurationDesviationOfTasks;

}
