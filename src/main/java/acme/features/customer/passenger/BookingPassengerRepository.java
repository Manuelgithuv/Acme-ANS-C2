
package acme.features.customer.passenger;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.BookingPassenger;

@Repository
public interface BookingPassengerRepository extends AbstractRepository {

	@Query("SELECT bp FROM BookingPassenger bp " + "WHERE bp.booking.id = :bookingId AND bp.passenger.id = :passengerId")
	BookingPassenger findByBookingIdAndPassengerId(@Param("bookingId") int bookingId, @Param("passengerId") int passengerId);

}
