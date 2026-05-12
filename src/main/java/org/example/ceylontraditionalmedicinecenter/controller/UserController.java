package org.example.ceylontraditionalmedicinecenter.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ceylontraditionalmedicinecenter.dto.AuthDTO;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.dto.UserDTO;
import org.example.ceylontraditionalmedicinecenter.service.UserService;
import org.example.ceylontraditionalmedicinecenter.util.JWTUtil;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Allows CORS requests from specified origins; here it permits any origin.
@CrossOrigin(origins = "*")
// Combines @Controller and @ResponseBody to expose REST endpoints returning JSON/XML.
@RestController
// Defines base URL mapping for the controller or a request mapping for a handler method.
@RequestMapping("api/v1/user")
// Lombok annotation that generates a constructor for final fields.
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    // Maps HTTP POST requests to this handler method.
    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            int res = userService.saveUser(userDTO);
            switch (res) {
                case VarList.Created -> {
                    String token = jwtUtil.generateToken(userDTO);
                    AuthDTO authDTO = new AuthDTO();
                    authDTO.setEmail(userDTO.getEmail());
                    authDTO.setToken(token);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Success", authDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Email Already Used", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    // Maps HTTP GET requests to this handler method.
    @GetMapping(value = "/getUserByEmail")
    public ResponseEntity<ResponseDTO> getUserDetail(@RequestParam String email) {

        UserDTO user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Success", user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Acceptable, "User not found", null));
        }
    }
    // Maps HTTP POST requests to this handler method.
    @PostMapping("/update-role")
    // Evaluates a security expression before allowing access to the method.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> updateUserRole(@RequestParam String email, @RequestParam String role) {
        boolean updated = userService.updateUserRole(email, role);

        if (updated) {
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "User role updated successfully", updated));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(VarList.Not_Acceptable, "User not found", null));
    }

    // Maps HTTP GET requests to this handler method.
    @GetMapping("/getAll")
    // Evaluates a security expression before allowing access to the method.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> getAllUsers(@RequestParam(required = false) String status) {
        List<UserDTO> users;
        if (status == null || status.isBlank()) {
            users = userService.getActiveUsers();
        } else if (status.equalsIgnoreCase("active")) {
            users = userService.getActiveUsers();
        } else {
            users = userService.getAllUsers();
        }

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, "No users found", null));
        }
        return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Users retrieved successfully", users));
    }

    // Maps HTTP DELETE requests to this handler method.
    @DeleteMapping("/delete/{email}")
    // Evaluates a security expression before allowing access to the method.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteUser(@PathVariable String email) {
        boolean deleted = userService.deleteUserByEmail(email);
        if (deleted) {
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "User deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
        }
    }

    // Maps HTTP PUT requests to this handler method.
    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateUser(@RequestParam String email, @RequestBody UserDTO userDTO) {
        try {
            boolean updated = userService.updateUser(email, userDTO);
            if (updated) {
                return ResponseEntity.ok(new ResponseDTO(VarList.Created, "User updated successfully", userDTO));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Acceptable, "User not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}
