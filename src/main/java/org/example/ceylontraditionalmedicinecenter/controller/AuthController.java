package org.example.ceylontraditionalmedicinecenter.controller;



import lombok.RequiredArgsConstructor;
import org.example.ceylontraditionalmedicinecenter.dto.AuthDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.dto.UserDTO;
import org.example.ceylontraditionalmedicinecenter.service.impl.UserServiceImpl;
import org.example.ceylontraditionalmedicinecenter.util.JWTUtil;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")  // any origin allow

public class AuthController {
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userService;
    private final ResponseDTO responseDTO;

    @PostMapping("/authentication")
    public ResponseEntity<ResponseDTO> authenticate(@RequestBody UserDTO userDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO(VarList.Unauthorized, "Invalid Credentials", e.getMessage()));
        }

        UserDTO loadedUser = userService.loadUserDetailsByUsername(userDTO.getEmail());
        if (loadedUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(VarList.Conflict, "Authorization Failure! Please Try Again", null));
        }

        String token = jwtUtil.generateToken(loadedUser);
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(VarList.Conflict, "Authorization Failure! Please Try Again", null));
        }

        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(loadedUser.getEmail());
        authDTO.setRole(loadedUser.getRole() == null ? "ROLE_USER" : loadedUser.getRole().trim().toUpperCase());
        if (!authDTO.getRole().startsWith("ROLE_")) {
            authDTO.setRole("ROLE_" + authDTO.getRole());
        }
        authDTO.setToken(token);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(VarList.Created, "Success", authDTO));
    }


}