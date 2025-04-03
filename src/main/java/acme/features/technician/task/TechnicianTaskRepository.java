
package acme.features.technician.task;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;

@Repository
public interface TechnicianTaskRepository extends AbstractRepository {

	@Query("select t from Task t")
	Collection<Task> findAllTasks();

	@Query("select t from Task t where t.technician.id = :technicianId ")
	Collection<Task> findAllTaskByTechnicianId(final int technicianId);

	@Query("select t from Task t where t.technician.id = :technicianId")
	Collection<Task> findTasksBytechnicianId(int technicianId);

	@Query("select i.task from Involves i where i.maintenanceRecord.id = :maintenanceRecordId")
	Collection<Task> findTasksByMaintenanceRecordId(int maintenanceRecordId);

	@Query("select t from Task t where t.id = :id")
	Task findTaskById(int id);

	@Query("select mr from MaintenanceRecord mr where mr.id = :id")
	MaintenanceRecord findOneMaintenanceRecordById(int id);
}
