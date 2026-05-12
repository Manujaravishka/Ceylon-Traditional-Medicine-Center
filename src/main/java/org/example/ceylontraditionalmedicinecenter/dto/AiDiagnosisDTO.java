package org.example.ceylontraditionalmedicinecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Lombok annotation that generates a constructor with one parameter for each field.
@AllArgsConstructor
// Lombok annotation that generates a no-argument constructor.
@NoArgsConstructor
// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
public class AiDiagnosisDTO {
    private Long id;
    private String symptoms;
    private String diagnosis;
    private String userEmail;
    private LocalDateTime createdAt;
    private String language;
}