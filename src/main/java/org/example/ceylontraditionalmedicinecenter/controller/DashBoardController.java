package org.example.ceylontraditionalmedicinecenter.controller;


import org.example.ceylontraditionalmedicinecenter.dto.DashBoardDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.service.AccommodationService;
import org.example.ceylontraditionalmedicinecenter.service.BookingService;
import org.example.ceylontraditionalmedicinecenter.service.DoctorService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// Combines @Controller and @ResponseBody to expose REST endpoints returning JSON/XML.
@RestController
// Defines base URL mapping for the controller or a request mapping for a handler method.
@RequestMapping("/api/v1/dashboard")
// Allows CORS requests from specified origins; here it permits any origin.
@CrossOrigin
public class DashBoardController {

    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private BookingService bookingService;

    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private AccommodationService accommodationService;

    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private DoctorService doctorService;

    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private org.example.ceylontraditionalmedicinecenter.service.UserService userService;

    // Maps HTTP GET requests to this handler method.
    @GetMapping("/counts")
    public ResponseEntity<ResponseDTO> getCounts() {
        try {
            DashBoardDTO dto = new DashBoardDTO();
            dto.setTotalBookings(bookingService.getTotalBookings());
            dto.setTotalAccommodations(accommodationService.getTotalAccommodationCount());
            dto.setTotalDoctors(doctorService.getTotalDoctorCount());
            dto.setTotalUsers((int) userService.getActiveUsersCount());

            return ResponseEntity.ok(
                    new ResponseDTO(VarList.Created, "Dashboard counts loaded successfully", dto)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    // Maps HTTP GET requests to this handler method.
    @GetMapping("/chart/booking-per-day")
    public ResponseEntity<ResponseDTO> getBookingPerDay() {
        try {
            List<Map<String,Object>> data = bookingService.getBookingsPerDay();
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Booking per day fetched", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    // Maps HTTP GET requests to this handler method.
    @GetMapping("/chart/total-price-per-booking")
    public ResponseEntity<ResponseDTO> getTotalPricePerBooking() {
        try {
            List<Map<String,Object>> data = bookingService.getTotalPricePerBooking();
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Total price per booking fetched", data));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}
