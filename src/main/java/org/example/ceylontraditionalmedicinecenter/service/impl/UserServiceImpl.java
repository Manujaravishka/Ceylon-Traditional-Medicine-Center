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
            user.setRole("USER");
        }
        userRepository.save(user);
        return VarList.Created;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    @Override
    public boolean updateUserRole(String email, String role) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        user.setRole(role);
        userRepository.save(user);
        return true;
    }

    @Override
    public boolean updateUser(String email, UserDTO userDTO) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
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
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        String role = user.getRole() == null ? "USER" : user.getRole();
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())))
                .build();
    }
}

