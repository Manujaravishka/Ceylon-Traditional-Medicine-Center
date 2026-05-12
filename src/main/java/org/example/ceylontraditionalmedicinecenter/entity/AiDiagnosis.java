package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Marks a class as a JPA entity mapped to a database table.
@Entity
// Specifies the table name for a JPA entity.
@Table(name = "ai_diagnosis")
// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
// Lombok annotation that generates a constructor with one parameter for each field.
@AllArgsConstructor
// Lombok annotation that generates a no-argument constructor.
@NoArgsConstructor
public class AiDiagnosis {
    // Marks a field as the primary key of the entity.
    @Id
    // Configures automatic ID generation strategy for the entity primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Configures a database column mapping for the entity field.
    @Column(nullable = false, length = 2000)
    private String symptoms;

    // Configures a database column mapping for the entity field.
    @Column(nullable = false, columnDefinition = "TEXT")
    private String diagnosis;

    // Configures a database column mapping for the entity field.
    @Column(nullable = true)
    private String userEmail;

    // Configures a database column mapping for the entity field.
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Configures a database column mapping for the entity field.
    @Column(nullable = false)
    private String language;
}