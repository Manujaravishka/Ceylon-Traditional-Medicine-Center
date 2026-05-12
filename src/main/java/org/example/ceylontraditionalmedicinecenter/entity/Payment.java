package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
// Marks a class as a JPA entity mapped to a database table.
@Entity
// Specifies the table name for a JPA entity.
@Table(name = "payments")
public class Payment {
    // Marks a field as the primary key of the entity.
    @Id
    // Configures automatic ID generation strategy for the entity primary key.
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
