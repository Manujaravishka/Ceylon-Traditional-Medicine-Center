package org.example.ceylontraditionalmedicinecenter.service;

import jakarta.validation.Valid;
import org.example.ceylontraditionalmedicinecenter.dto.UserDTO;

import java.util.List;

public interface UserService {
    int saveUser(UserDTO userDTO);

    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers();

    boolean deleteUserByEmail(String email);

    boolean updateUserRole(String email, String role);

    boolean updateUser(String email, UserDTO userDTO);

    UserDTO loadUserDetailsByUsername(String email);
}
