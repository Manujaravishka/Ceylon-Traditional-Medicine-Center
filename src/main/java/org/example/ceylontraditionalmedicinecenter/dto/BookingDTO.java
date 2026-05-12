package org.example.ceylontraditionalmedicinecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
// Lombok annotation that generates a constructor with one parameter for each field.
@AllArgsConstructor
// Lombok annotation that generates a no-argument constructor.
@NoArgsConstructor
public class BookingDTO {
    private String packageName;
    private String doctorEmail;
    private String accommodationName;
    private String userEmail;

    private int estimateDays;
    private String bookingDate;
    private String checkoutDate;


    private double packagePrice;
    private double doctorFee;
    private double accommodationFee;
    private double totalPrice;
}
