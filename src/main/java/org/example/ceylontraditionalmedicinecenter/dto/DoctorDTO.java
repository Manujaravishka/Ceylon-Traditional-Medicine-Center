package org.example.ceylontraditionalmedicinecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
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
