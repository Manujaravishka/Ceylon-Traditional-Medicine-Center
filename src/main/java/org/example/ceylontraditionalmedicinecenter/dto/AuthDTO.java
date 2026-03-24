package org.example.ceylontraditionalmedicinecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class AuthDTO {
    public String email;
    public String token;
    public String role;
}
