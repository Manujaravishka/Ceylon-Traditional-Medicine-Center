package org.example.ceylontraditionalmedicinecenter.service.impl;

import org.example.ceylontraditionalmedicinecenter.dto.BookingDTO;
import org.example.ceylontraditionalmedicinecenter.entity.Accommodation;
import org.example.ceylontraditionalmedicinecenter.entity.Booking;
import org.example.ceylontraditionalmedicinecenter.entity.Doctor;
import org.example.ceylontraditionalmedicinecenter.entity.TreatPackage;
import org.example.ceylontraditionalmedicinecenter.entity.User;
import org.example.ceylontraditionalmedicinecenter.repository.AccommodationRepository;
import org.example.ceylontraditionalmedicinecenter.repository.BookingRepository;
import org.example.ceylontraditionalmedicinecenter.repository.DoctorRepository;
import org.example.ceylontraditionalmedicinecenter.repository.TreatPackageRepository;
import org.example.ceylontraditionalmedicinecenter.repository.UserRepository;
import org.example.ceylontraditionalmedicinecenter.service.BookingService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final TreatPackageRepository treatPackageRepository;

    @Autowired
    private final AccommodationRepository accommodationRepository;

    @Autowired
    private final DoctorRepository doctorRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              UserRepository userRepository,
                              TreatPackageRepository treatPackageRepository,
                              AccommodationRepository accommodationRepository,
                              DoctorRepository doctorRepository,
                              ModelMapper modelMapper) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.treatPackageRepository = treatPackageRepository;
        this.accommodationRepository = accommodationRepository;
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean saveBooking(BookingDTO bookingDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(bookingDTO.getUserEmail());
        Optional<TreatPackage> optionalPackage = treatPackageRepository.findByName(bookingDTO.getPackageName());
        Optional<Accommodation> optionalAccommodation = accommodationRepository.findByName(bookingDTO.getAccommodationName());
        Optional<Doctor> optionalDoctor = doctorRepository.findByEmail(bookingDTO.getDoctorEmail());

        if (optionalUser.isEmpty() || optionalPackage.isEmpty() || optionalAccommodation.isEmpty() || optionalDoctor.isEmpty()) {
            return false;
        }

        Booking booking = new Booking();
        booking.setUser(optionalUser.get());
        booking.setTreatPackage(optionalPackage.get());
        booking.setAccommodation(optionalAccommodation.get());
        booking.setDoctor(optionalDoctor.get());
        booking.setEstimateDays(bookingDTO.getEstimateDays());
        booking.setBookingDate(LocalDate.parse(bookingDTO.getBookingDate()));
        booking.setCheckoutDate(LocalDate.parse(bookingDTO.getCheckoutDate()));
        booking.setPackagePrice(bookingDTO.getPackagePrice());
        booking.setAccommodationPrice(bookingDTO.getAccommodationFee());
        booking.setDoctorFee(bookingDTO.getDoctorFee());
        booking.setTotalPrice(bookingDTO.getTotalPrice());
        booking.setActive(true);

        bookingRepository.save(booking);
        return true;
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingByUserEmail(String email) {
        return bookingRepository.findByUserEmail(email).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public int getTotalBookings() {
        return (int) bookingRepository.count();
    }

    @Override
    public List<Map<String, Object>> getBookingsPerDay() {
        return bookingRepository.findBookingsPerDay();
    }

    @Override
    public List<Map<String, Object>> getTotalPricePerBooking() {
        return bookingRepository.findTotalPricePerBooking();
    }

    private BookingDTO toDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setUserEmail(booking.getUser().getEmail());
        dto.setPackageName(booking.getTreatPackage().getName());
        dto.setAccommodationName(booking.getAccommodation().getName());
        dto.setDoctorEmail(booking.getDoctor().getEmail());
        dto.setEstimateDays(booking.getEstimateDays());
        dto.setBookingDate(booking.getBookingDate().toString());
        dto.setCheckoutDate(booking.getCheckoutDate().toString());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setDoctorFee(booking.getDoctorFee());
        dto.setAccommodationFee(booking.getAccommodationPrice());
        dto.setPackagePrice(booking.getPackagePrice());
        return dto;
    }
}

