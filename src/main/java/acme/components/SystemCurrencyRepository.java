package acme.components;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.system_configuration.SystemConfiguration;

@Repository
public interface SystemCurrencyRepository extends AbstractRepository {

	@Query("select sc from SystemConfiguration sc")
	List<SystemConfiguration> getSystemConfiguration();
}