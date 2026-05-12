package org.example.ceylontraditionalmedicinecenter.config;

import org.example.ceylontraditionalmedicinecenter.entity.Accommodation;
import org.example.ceylontraditionalmedicinecenter.entity.Booking;
import org.example.ceylontraditionalmedicinecenter.entity.Doctor;
import org.example.ceylontraditionalmedicinecenter.repository.AccommodationRepository;
import org.example.ceylontraditionalmedicinecenter.repository.BookingRepository;
import org.example.ceylontraditionalmedicinecenter.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

// Marks this class as a Spring-managed component for component scanning.
@Component
public class BookingStatusScheduler {

    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private BookingRepository bookingRepository;

    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private AccommodationRepository accommodationRepository;

    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private DoctorRepository doctorRepository;

    // @Scheduled annotation used here.
    @Scheduled(cron = "0 * * * * ?")
    public void updateBookingStatus() {
        LocalDate today = LocalDate.now();

        List<Booking> completedBookings = bookingRepository.findByCheckoutDateLessThanEqualAndActive(today,true);

        for (Booking booking : completedBookings) {
            Accommodation accommodation = booking.getAccommodation();
            Doctor doctor = booking.getDoctor();
            if (accommodation != null) {
                accommodation.setBooked("NO");
                accommodationRepository.save(accommodation);
            }
            if (doctor != null) {
                doctor.setBooked("NO");
                doctorRepository.save(doctor);
            }
            booking.setActive(false);
            bookingRepository.save(booking);
        }
    }
}
