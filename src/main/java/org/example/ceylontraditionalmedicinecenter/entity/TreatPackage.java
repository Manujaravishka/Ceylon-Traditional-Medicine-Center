package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

// Marks a class as a JPA entity mapped to a database table.
@Entity
// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
// Specifies the table name for a JPA entity.
@Table(name = "package")
public class TreatPackage {

    // Marks a field as the primary key of the entity.
    @Id
    // Configures automatic ID generation strategy for the entity primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    private Integer estimateDays;

    // Defines a many-to-many JPA relationship.
    @ManyToMany(fetch = FetchType.EAGER)
    // @JoinTable annotation used here.
    @JoinTable(
            name = "package_activity",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "activity_id")
    )
    private List<Activity> activities;

    private String imageUrl;

    private Integer sold = 0;

}
