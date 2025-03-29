
package acme.features.customer.booking;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("SELECT b from Booking b WHERE b.customer.id=:customerId")
	List<Booking> findBookingsByCustomerId(@Param("customerId") int customerId);

	@Query("SELECT b from Booking b WHERE b.id=:bookingId")
	Booking findById(@Param("bookingId") int bookingId);

	@Query("SELECT b FROM Booking b WHERE b.locatorCode = :locatorCode AND b.id <> :bookingId")
	Optional<Booking> findBookingsByLocatorCode(@Param("locatorCode") String locatorCode, @Param("bookingId") int bookingId);

}
