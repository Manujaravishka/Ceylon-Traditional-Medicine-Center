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
public class DashBoardDTO {

    private int totalBookings;
    private int totalAccommodations;
    private int totalDoctors;
    private int totalUsers;
}