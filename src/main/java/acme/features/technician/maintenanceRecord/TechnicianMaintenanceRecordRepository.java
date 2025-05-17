
package acme.features.technician.maintenanceRecord;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.datatypes.AircraftStatus;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@Repository
public interface TechnicianMaintenanceRecordRepository extends AbstractRepository {

	@Query("select m from MaintenanceRecord m")
	Collection<MaintenanceRecord> findAllMaintenanceRecords();

	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :technicianId ")
	Collection<MaintenanceRecord> findAllMaintenanceRecordByTechnicianId(final int technicianId);

	@Query("select a from Aircraft a")
	Collection<Aircraft> findAllAircrafts();

	@Query("select mr from MaintenanceRecord mr where mr.id = :maintenanceRecordId")
	MaintenanceRecord findMaintenanceRecordById(int maintenanceRecordId);

	@Query("select mr from MaintenanceRecord mr where mr.draftMode = false")
	Collection<MaintenanceRecord> findAllMaintenanceRecordsPublished();

	@Query("select a from Aircraft a where a.status = :status")
	List<Aircraft> findAircraftsInMaintenance(@Param("status") AircraftStatus status);

	@Query("""
		    SELECT a
		    FROM Aircraft a
		    WHERE a.status = :status
		    OR a.id = :maintenanceRecordAircraftId
		""")
	List<Aircraft> findAircraftsUnderMaintenanceOrSpecific(@Param("status") AircraftStatus status, @Param("maintenanceRecordAircraftId") int maintenanceRecordAircraftId);
}
