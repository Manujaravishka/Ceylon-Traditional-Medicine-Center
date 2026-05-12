package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Marks a class as a JPA entity mapped to a database table.
@Entity
// Lombok annotation that generates a constructor with one parameter for each field.
@AllArgsConstructor
// Lombok annotation that generates a no-argument constructor.
@NoArgsConstructor
// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
// Specifies the table name for a JPA entity.
@Table(name = "doctors")
public class Doctor {
    // Marks a field as the primary key of the entity.
    @Id
    // Configures automatic ID generation strategy for the entity primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int did;

    private String fullName;
    private String description;
    // Configures a database column mapping for the entity field.
    @Column(unique = true, nullable = false)
    private String email;
    private String imageUrl;
    private String linkedin;
    private String paymentPerDay;
    private String status;
    private String booked;
}
