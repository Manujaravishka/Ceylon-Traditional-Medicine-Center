package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail;
    private String cardHolderName;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private double amount;

    private LocalDateTime paymentDate;
}
