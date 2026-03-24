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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

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
