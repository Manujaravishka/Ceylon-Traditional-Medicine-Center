package org.example.ceylontraditionalmedicinecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

// Lombok annotation that generates a constructor with one parameter for each field.
@AllArgsConstructor
// Lombok annotation that generates a no-argument constructor.
@NoArgsConstructor
// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
// Marks this class as a Spring-managed component for component scanning.
@Component
public class ReviewDTO {
    private String userEmail;
    private Long id;
    private String comment;
    private String createdAt;

    private String problem;
    private boolean solved;

    private int rating;
}
