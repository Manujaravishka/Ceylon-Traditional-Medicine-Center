package org.example.ceylontraditionalmedicinecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AiDiagnosisDTO {
    private Long id;
    private String symptoms;
    private String diagnosis;
    private String userEmail;
    private LocalDateTime createdAt;
    private String language;
}