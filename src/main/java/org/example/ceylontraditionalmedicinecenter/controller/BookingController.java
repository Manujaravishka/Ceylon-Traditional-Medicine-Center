package org.example.ceylontraditionalmedicinecenter.controller;



import org.example.ceylontraditionalmedicinecenter.dto.BookingDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.service.BookingService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Allows CORS requests from specified origins; here it permits any origin.
@CrossOrigin(origins = "*")
// Combines @Controller and @ResponseBody to expose REST endpoints returning JSON/XML.
@RestController
// Defines base URL mapping for the controller or a request mapping for a handler method.
@RequestMapping("api/v1/booking")
public class BookingController {
    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private BookingService bookingService;

    // Maps HTTP POST requests to this handler method.
    @PostMapping("/save")
    public ResponseEntity<ResponseDTO>saveBooking(@RequestBody BookingDTO bookingDTO) {
        try{
            boolean isSaved = bookingService.saveBooking(bookingDTO);

            if(isSaved){
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created,"Booking Saved Successfully",bookingDTO));
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.Not_Acceptable,"Booking Not Saved",null));
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,e.getMessage(),null));
        }
    }

    // Maps HTTP GET requests to this handler method.
    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllBookings() {
        try{
            List<BookingDTO> allBookings = bookingService.getAllBookings();
            return ResponseEntity.ok(new ResponseDTO(
                    VarList.Created,"Booking Restrieved Successfully",allBookings));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error,e.getMessage(),null));
        }
    }

    // Maps HTTP GET requests to this handler method.
    @GetMapping("/getBooking/{email}")
    public ResponseEntity<List<BookingDTO>> getBookingByUserEmail(@PathVariable String email) {
        List<BookingDTO> bookings = bookingService.getBookingByUserEmail(email);
        return ResponseEntity.ok(bookings);
    }
}
