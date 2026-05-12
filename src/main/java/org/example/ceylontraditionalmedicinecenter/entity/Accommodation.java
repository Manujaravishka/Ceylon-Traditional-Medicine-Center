package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Marks a class as a JPA entity mapped to a database table.
@Entity
// Specifies the table name for a JPA entity.
@Table(name = "accommodation")
// Lombok annotation that generates a no-argument constructor.
@NoArgsConstructor
// Lombok annotation that generates a constructor with one parameter for each field.
@AllArgsConstructor
// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data

public class Accommodation {
    // Marks a field as the primary key of the entity.
    @Id
    // Configures automatic ID generation strategy for the entity primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Configures a database column mapping for the entity field.
    @Column(nullable = false,unique = true)
    private String name;

    // Configures a database column mapping for the entity field.
    @Column(length = 500)
    private String description;

    private String imageUrl;

    private String location;

    private String category;

    private String CostPerDay;

    private String booked;     //parse booking
}
