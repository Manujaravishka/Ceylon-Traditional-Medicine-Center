package org.example.ceylontraditionalmedicinecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
// Lombok annotation that generates a constructor with one parameter for each field.
@AllArgsConstructor
public class APIResponse {
    private int status;
    private String message;
    private Object data;
}
