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

@Component
public class BookingStatusScheduler {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private DoctorRepository doctorRepository;

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
