package org.example.ceylontraditionalmedicinecenter.controller;



import jakarta.validation.Valid;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ReviewDTO;
import org.example.ceylontraditionalmedicinecenter.service.ReviewService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Allows CORS requests from specified origins; here it permits any origin.
@CrossOrigin(origins = "*")
// Combines @Controller and @ResponseBody to expose REST endpoints returning JSON/XML.
@RestController
// Defines base URL mapping for the controller or a request mapping for a handler method.
@RequestMapping("api/v1/review")
public class ReviewController {
    // Injects a dependency automatically by type from the Spring context.
    @Autowired
    private ReviewService reviewService;

    // Maps HTTP POST requests to this handler method.
    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> addReview(@RequestBody @Valid ReviewDTO reviewDTO) {
        try {
            int res = reviewService.saveReview(reviewDTO);
            return switch (res) {
                case VarList.Created -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Review added successfully", reviewDTO));
                case VarList.Not_Acceptable -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.Not_Acceptable, "Invalid user or rating", null));
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO(VarList.Internal_Server_Error, "An error occurred", null));
            };
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Server error: " + e.getMessage(), null));
        }
    }

    // ✅ Get all reviews
    // Maps HTTP GET requests to this handler method.
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> getAllReviews() {
        try {
            return ResponseEntity.ok(
                    new ResponseDTO(VarList.Created, "Reviews fetched successfully", reviewService.getAllReviews())
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Server error: " + e.getMessage(), null));
        }
    }

    // Maps HTTP DELETE requests to this handler method.
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteReview(@PathVariable Long id) {
        try {
            int res = reviewService.deleteReview(id);
            return switch (res) {
                case VarList.Created -> ResponseEntity.ok(
                        new ResponseDTO(VarList.Created, "Review deleted successfully", null));
                case VarList.Not_Found -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Review not found", null));
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO(VarList.Internal_Server_Error, "An error occurred", null));
            };
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, "Server error: " + e.getMessage(), null));
        }
    }

}
