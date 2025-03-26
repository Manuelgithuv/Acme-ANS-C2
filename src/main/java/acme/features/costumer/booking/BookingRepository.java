
package acme.features.costumer.booking;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;

@Repository
public interface BookingRepository extends AbstractRepository {

	@Query("SELECT b from Booking b WHERE b.customer.id=:customerId")
	List<Booking> findBookingsByCustomerId(@Param("customerId") int customerId);

	@Query("SELECT b from Booking b WHERE b.id=:bookingId")
	Booking findById(@Param("bookingId") int bookingId);

	@Query("SELECT f FROM Flight f WHERE f.published=true")
	List<Flight> findPublicFlights();

}
