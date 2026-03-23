package org.example.ceylontraditionalmedicinecenter.repository;


import org.example.ceylontraditionalmedicinecenter.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByUserEmail(String email);

    List<Booking> findByCheckoutDateLessThanEqualAndActive(LocalDate checkoutDateIsGreaterThan, boolean active);

    @Query(value = "SELECT booking_date AS date, COUNT(*) AS count FROM booking GROUP BY booking_date", nativeQuery = true)
    List<Map<String, Object>> findBookingsPerDay();

    @Query(value = "SELECT id AS bookingId, total_price AS total FROM booking", nativeQuery = true)
    List<Map<String, Object>> findTotalPricePerBooking();
}
