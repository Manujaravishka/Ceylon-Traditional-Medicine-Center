package org.example.ceylontraditionalmedicinecenter.service;

import org.example.ceylontraditionalmedicinecenter.dto.BookingDTO;

import java.util.List;
import java.util.Map;

public interface BookingService {
    boolean saveBooking(BookingDTO bookingDTO);

    List<BookingDTO> getAllBookings();

    List<BookingDTO> getBookingByUserEmail(String email);

    int getTotalBookings();

    List<Map<String, Object>> getBookingsPerDay();

    List<Map<String, Object>> getTotalPricePerBooking();
}
