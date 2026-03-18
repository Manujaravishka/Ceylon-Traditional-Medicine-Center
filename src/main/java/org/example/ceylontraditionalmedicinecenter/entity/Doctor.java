package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int did;

    private String fullName;
    private String description;
    @Column(unique = true, nullable = false)
    private String email;
    private String imageUrl;
    private String linkedin;
    private String paymentPerDay;
    private String status;
    private String booked;
}
