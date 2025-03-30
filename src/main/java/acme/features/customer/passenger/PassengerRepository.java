
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;

@Repository
public interface PassengerRepository extends AbstractRepository {

	@Query("SELECT p FROM Passenger p JOIN BookingPassenger bp ON bp.passenger.id = p.id JOIN Booking b ON bp.booking.id = b.id WHERE b.customer.id = :customerId")
	List<Passenger> findPassengersByCustomerId(@Param("customerId") int customerId);

	@Query("SELECT bp.passenger FROM BookingPassenger bp WHERE bp.booking.id = :bookingId")
	List<Passenger> findPassengersByBookingId(@Param("bookingId") int bookingId);

	@Query("SELECT p FROM Passenger p")
	List<Passenger> findAllPassengers();

	@Query("SELECT p from Passenger p WHERE p.id =:passengerId")
	Passenger findById(@Param("passengerId") int passengerId);

}
