package org.example.ceylontraditionalmedicinecenter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Lombok annotation that generates a constructor with one parameter for each field.
@AllArgsConstructor
// Lombok annotation that generates a no-argument constructor.
@NoArgsConstructor
// Lombok annotation that generates getters, setters, equals/hashCode, and toString.
@Data
public class UserDTO {
    // @NotBlank annotation used here.
    @NotBlank(message = "Name is required")
    // @Size annotation used here.
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    // @NotBlank annotation used here.
    @NotBlank(message = "Email is required")
    // @Email annotation used here.
    @Email(message = "Email must be valid")
    private String email;

    private String contact;

    // @NotBlank annotation used here.
    @NotBlank(message = "Password is required")
    // @Size annotation used here.
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String role;
    private String status;
}
