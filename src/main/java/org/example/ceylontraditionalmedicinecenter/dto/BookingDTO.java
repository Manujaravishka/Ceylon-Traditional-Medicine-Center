package org.example.ceylontraditionalmedicinecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
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
