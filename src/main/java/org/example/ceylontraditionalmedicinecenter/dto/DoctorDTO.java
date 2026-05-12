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
public class DoctorDTO {
    private String fullName;
    private String description;
    private String email;
    private String imageUrl;
    private String linkedin;
    private String paymentPerDay;
    private String status;
    private String booked;
}
