
package acme.features.authenticated.customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.Customer;

@Repository
public interface AuthenticatedCustomerRepository extends AbstractRepository {

	@Query("select c from Customer c where c.userAccount.id = :id")
	Customer findOneCustomerByUserAccountId(int id);

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("select c from Customer c where c.identifier=:identifier")
	Optional<Customer> findByIdentifier(@Param("identifier") String identifier);

}
