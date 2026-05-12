package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


// Marks a class as a JPA entity mapped to a database table.
@Entity
// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
// Lombok annotation that generates a constructor with one parameter for each field.
@AllArgsConstructor
// Lombok annotation that generates a no-argument constructor.
@NoArgsConstructor
// @Builder annotation used here.
@Builder
public class User implements Serializable {
    // Marks a field as the primary key of the entity.
    @Id
    // Configures automatic ID generation strategy for the entity primary key.
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;
    private String name;
    // Configures a database column mapping for the entity field.
    @Column(unique = true)
    private String email;
    private String contact;
    private String password;
    private String role;
    private String status;
}