package org.example.ceylontraditionalmedicinecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lombok annotation that generates a constructor with one parameter for each field.
@AllArgsConstructor
// Lombok annotation that generates a no-argument constructor.
@NoArgsConstructor
// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
public class PaymentDTO {
    private String userEmail;
    private String cardHolderName;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private double amount;
}