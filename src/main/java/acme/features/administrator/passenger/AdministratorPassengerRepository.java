
package acme.features.administrator.passenger;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;

@Repository
public interface AdministratorPassengerRepository extends AbstractRepository {

	@Query("SELECT p FROM Passenger p  WHERE p.customer.id = :customerId OR p.published = true ")
	List<Passenger> findPassengersByCustomerId(@Param("customerId") int customerId);

	@Query("SELECT bp.passenger FROM BookingPassenger bp WHERE bp.booking.id = :bookingId")
	List<Passenger> findPassengersByBookingId(@Param("bookingId") int bookingId);

	@Query("SELECT p FROM Passenger p")
	List<Passenger> findAllPassengers(@Param("customerId") int customerId);

	@Query("SELECT p FROM Passenger p WHERE p.published = true OR p.customer.id = :customerId")
	Collection<Passenger> findAvailablePassengers(@Param("customerId") int customerId);

	@Query("SELECT p from Passenger p WHERE p.id =:passengerId")
	Passenger findById(@Param("passengerId") int passengerId);

}
