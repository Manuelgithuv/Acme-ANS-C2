
package acme.features.technician.involves;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.involves.Involves;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;
import acme.realms.Technician;

@Repository
public interface TechnicianInvolvesRepository extends AbstractRepository {

	@Query("select i from Involves i where i.maintenanceRecord.id = :maintenanceRecordId")
	Collection<Involves> findAllInvolvesByMaintenanceRecordId(final int maintenanceRecordId);

	@Query("select i from Involves i where i.task.id = :taskId")
	Collection<Involves> findAllInvolvesByTaskId(final int taskId);

	@Query("select mr from MaintenanceRecord mr where mr.id = :maintenanceRecordId")
	MaintenanceRecord findMaintenanceRecordById(int maintenanceRecordId);

	@Query("select t from Task t where t not in (select i.task from Involves i where i.maintenanceRecord = :maintenanceRecord) and (t.draftMode = false or t.technician = :technician)")
	Collection<Task> findValidTasksToAdd(MaintenanceRecord maintenanceRecord, Technician technician);

	@Query("select t from Task t where t in (select i.task from Involves i where i.maintenanceRecord = :maintenanceRecord)")
	Collection<Task> findValidTasksToRemove(MaintenanceRecord maintenanceRecord);

	@Query("select i from Involves i where i.maintenanceRecord = :maintenanceRecord and i.task = :task")
	Involves findInvolvesByMaintenanceRecordTask(MaintenanceRecord maintenanceRecord, Task task);

}
