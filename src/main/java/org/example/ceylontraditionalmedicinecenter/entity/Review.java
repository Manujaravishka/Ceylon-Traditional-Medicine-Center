package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Marks a class as a JPA entity mapped to a database table.
@Entity
// Specifies the table name for a JPA entity.
@Table(name ="review" )
// Lombok annotation that generates a constructor with one parameter for each field.
@AllArgsConstructor
// Lombok annotation that generates a no-argument constructor.
@NoArgsConstructor
// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
public class Review {
    // Marks a field as the primary key of the entity.
    @Id
    // Configures automatic ID generation strategy for the entity primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Configures a database column mapping for the entity field.
    @Column(nullable = false)
    private String userEmail; // Review එක ලබා දුන් user ගේ email

    // Configures a database column mapping for the entity field.
    @Column(nullable = false)
    private String comment; // Review content

    // Configures a database column mapping for the entity field.
    @Column(nullable = true)
    private String problem; // Health problem described

    // Configures a database column mapping for the entity field.
    @Column(nullable = false)
    private boolean solved; // Whether the problem was solved

    // Configures a database column mapping for the entity field.
    @Column(nullable = false)
    private int rating; // Rating (e.g., 1-5)

    // Configures a database column mapping for the entity field.
    @Column(nullable = false)
    private String createdAt; // Review එක ලබා දුන් date/time
}
