package org.example.ceylontraditionalmedicinecenter.service.impl;

import org.example.ceylontraditionalmedicinecenter.dto.UserDTO;
import org.example.ceylontraditionalmedicinecenter.entity.User;
import org.example.ceylontraditionalmedicinecenter.repository.UserRepository;
import org.example.ceylontraditionalmedicinecenter.service.UserService;
import org.example.ceylontraditionalmedicinecenter.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public int saveUser(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return VarList.Not_Acceptable;
        }
        User user = modelMapper.map(userDTO, User.class);
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("ROLE_USER");
        } else {
            String normalized = user.getRole().trim().toUpperCase();
            if (!normalized.startsWith("ROLE_")) {
                normalized = "ROLE_" + normalized;
            }
            user.setRole(normalized);
        }
        userRepository.save(user);
        return VarList.Created;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return null;
        }
        return modelMapper.map(optionalUser.get(), UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return false;
        }
        userRepository.delete(optionalUser.get());
        return true;
    }

    @Override
    public boolean updateUserRole(String email, String role) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return false;
        }
        User user = optionalUser.get();
        if (role == null || role.isBlank()) {
            return false;
        }
        String normalized = role.trim().toUpperCase();
        if (!normalized.startsWith("ROLE_")) {
            normalized = "ROLE_" + normalized;
        }
        user.setRole(normalized);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateUser(String email, UserDTO userDTO) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return false;
        }
        User user = optionalUser.get();
        user.setName(userDTO.getName());
        user.setContact(userDTO.getContact());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        }
        if (userDTO.getRole() != null && !userDTO.getRole().isBlank()) {
            user.setRole(userDTO.getRole());
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public UserDTO loadUserDetailsByUsername(String email) {
        return getUserByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        String role = user.getRole() == null ? "ROLE_USER" : user.getRole().trim().toUpperCase();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(role)))
                .build();
    }
}

