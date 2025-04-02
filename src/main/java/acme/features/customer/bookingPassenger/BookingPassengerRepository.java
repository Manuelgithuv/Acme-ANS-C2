
package acme.features.customer.bookingPassenger;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingPassenger;

@Repository
public interface BookingPassengerRepository extends AbstractRepository {

	@Query("SELECT bp from BookingPassenger bp WHERE bp.customer.id=:customerId")
	List<BookingPassenger> findBookingPassengersByCustomerId(@Param("customerId") int customerId);

	@Query("SELECT bp from BookingPassenger bp WHERE bp.id=:bookingPassengerId")
	BookingPassenger findById(@Param("bookingPassengerId") int bookingPassengerId);

	@Query("SELECT b FROM Booking b WHERE b.locatorCode = :locatorCode AND b.id <> :bookingId")
	Optional<Booking> findBookingsByLocatorCode(@Param("locatorCode") String locatorCode, @Param("bookingId") int bookingId);

}
