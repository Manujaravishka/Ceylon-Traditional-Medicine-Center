package org.example.ceylontraditionalmedicinecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentDTO {
    private String userEmail;
    private String cardHolderName;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private double amount;
}