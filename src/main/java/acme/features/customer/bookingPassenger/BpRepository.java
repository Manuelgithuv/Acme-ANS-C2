
package acme.features.customer.bookingPassenger;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.BookingPassenger;

@Repository
public interface BpRepository extends AbstractRepository {

	@Query("SELECT bp from BookingPassenger bp WHERE bp.customer.id=:customerId OR bp.published=true")
	List<BookingPassenger> findBookingPassengersByCustomerId(@Param("customerId") int customerId);

	@Query("SELECT bp from BookingPassenger bp WHERE bp.booking.id=:bookingId")
	List<BookingPassenger> findBookingPassengersByBookingId(@Param("bookingId") int bookingId);

	@Query("SELECT bp from BookingPassenger bp WHERE bp.id=:bookingPassengerId")
	BookingPassenger findById(@Param("bookingPassengerId") int bookingPassengerId);

	@Query("SELECT bp FROM BookingPassenger bp WHERE bp.passenger.id = :passengerId AND bp.booking.id = :bookingId")
	Optional<BookingPassenger> findBookingPassengerByPassengerAndBooking(@Param("passengerId") int passengerId, @Param("bookingId") int bookingId);

}
