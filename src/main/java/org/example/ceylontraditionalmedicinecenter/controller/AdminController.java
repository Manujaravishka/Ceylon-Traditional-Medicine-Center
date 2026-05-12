package org.example.ceylontraditionalmedicinecenter.controller;



import lombok.RequiredArgsConstructor;
import org.example.ceylontraditionalmedicinecenter.dto.ResponseDTO;
import org.example.ceylontraditionalmedicinecenter.dto.UserDTO;
import org.example.ceylontraditionalmedicinecenter.service.UserService;
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
@RequestMapping("api/v1/admin")
// Lombok annotation that generates a constructor for final fields.
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    // Maps HTTP GET requests to this handler method.
    @GetMapping("/admin")
    // Evaluates a security expression before allowing access to the method.
    @PreAuthorize("hasRole('ADMIN')")
    public String helloAdmin() {
        return "Hello Admin";
    }

    // Maps HTTP GET requests to this handler method.
    @GetMapping("/user")
    // Evaluates a security expression before allowing access to the method.
    @PreAuthorize("hasRole('USER')")
    public String helloUser() {
        return "Hello User";
    }

    // Maps HTTP POST requests to this handler method.
    @PostMapping("/create-user")
    // Evaluates a security expression before allowing access to the method.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody UserDTO userDTO) {
        try {
            int res = userService.saveUser(userDTO);
            switch (res) {
                case VarList.Created -> {
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "User created successfully", null));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Email already exists", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error creating user", null));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    // Maps HTTP GET requests to this handler method.
    @GetMapping("/users")
    // Evaluates a security expression before allowing access to the method.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Users retrieved successfully", users));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    // Maps HTTP PUT requests to this handler method.
    @PutMapping("/update-role")
    // Evaluates a security expression before allowing access to the method.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> updateUserRole(@RequestParam String email, @RequestParam String role) {
        try {
            boolean updated = userService.updateUserRole(email, role);
            if (updated) {
                return ResponseEntity.ok(new ResponseDTO(VarList.Created, "User role updated successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    // Maps HTTP DELETE requests to this handler method.
    @DeleteMapping("/delete-user")
    // Evaluates a security expression before allowing access to the method.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseDTO> deleteUser(@RequestParam String email) {
        try {
            boolean deleted = userService.deleteUserByEmail(email);
            if (deleted) {
                return ResponseEntity.ok(new ResponseDTO(VarList.Created, "User deleted successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}
