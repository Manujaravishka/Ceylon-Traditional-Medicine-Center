package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Marks a class as a JPA entity mapped to a database table.
@Entity
// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
// Lombok annotation that generates a constructor with one parameter for each field.
@AllArgsConstructor
// Lombok annotation that generates a no-argument constructor.
@NoArgsConstructor
// Specifies the table name for a JPA entity.
@Table(name = "booking")
public class Booking {
    // Marks a field as the primary key of the entity.
    @Id
    // Configures automatic ID generation strategy for the entity primary key.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    -----Relationships--------

    // Defines a many-to-one JPA relationship.
    @ManyToOne
    // Defines the foreign key column in a JPA association.
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Defines a many-to-one JPA relationship.
    @ManyToOne
    // Defines the foreign key column in a JPA association.
    @JoinColumn(name = "package_id",nullable = false)
    private TreatPackage treatPackage;

    // Defines a many-to-one JPA relationship.
    @ManyToOne
    // Defines the foreign key column in a JPA association.
    @JoinColumn(name = "accommodation_id",nullable = false)
    private Accommodation accommodation;

    // Defines a many-to-one JPA relationship.
    @ManyToOne
    // Defines the foreign key column in a JPA association.
    @JoinColumn(name = "doctor_id",referencedColumnName = "did",nullable = false)
    private Doctor doctor;

    //    --------Booking Details----------
    private int estimateDays;

    private LocalDate bookingDate;
    private LocalDate checkoutDate;

    private double packagePrice;
    private double accommodationPrice;
    private double doctorFee;
    private double totalPrice;

    // Configures a database column mapping for the entity field.
    @Column(nullable = false)
    private boolean active = true;
}
