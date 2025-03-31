
package acme.features.technician.Involves;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.involves.Involves;

@Repository
public interface TechnicianInvolvesRepository extends AbstractRepository {

	@Query("select i from Involves i where i.maintenanceRecord.id = :maintenanceRecordId")
	Collection<Involves> findAllInvolvesByMaintenanceRecordId(final int maintenanceRecordId);

	@Query("select i from Involves i where i.task.id = :taskId")
	Collection<Involves> findAllInvolvesByTaskId(final int taskId);

}
