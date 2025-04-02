
package acme.features.authenticated.technician;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.Manager;

@Repository
public interface AuthenticatedTechnicianRepository extends AbstractRepository {

	@Query("select t from Technician t where t.userAccount.id = :id")
	Manager findOneManagerByUserAccountId(int id);

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("select t from Technician t where t.licenseNumber=:licenseNumber")
	Optional<Manager> findByLicenseNumber(@Param("licenseNumber") String licenseNumber);

}
