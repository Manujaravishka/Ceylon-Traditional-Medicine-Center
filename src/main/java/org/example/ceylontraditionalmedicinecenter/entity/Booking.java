package org.example.ceylontraditionalmedicinecenter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    -----Relationships--------

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "package_id",nullable = false)
    private TreatPackage treatPackage;

    @ManyToOne
    @JoinColumn(name = "accommodation_id",nullable = false)
    private Accommodation accommodation;

    @ManyToOne
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

    @Column(nullable = false)
    private boolean active = true;
}
