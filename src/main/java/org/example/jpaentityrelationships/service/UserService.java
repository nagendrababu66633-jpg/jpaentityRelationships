
        package org.example.jpaentityrelationships.service;


import org.example.jpaentityrelationships.dto.UserRequest;
import org.example.jpaentityrelationships.dto.UserResponse;
import org.example.jpaentityrelationships.entity.Role;
import org.example.jpaentityrelationships.entity.TopCustomerResponse;
import org.example.jpaentityrelationships.entity.User;
import org.example.jpaentityrelationships.exceptions.DuplicateResourceException;
import org.example.jpaentityrelationships.exceptions.ResourceNotFoundException;
import org.example.jpaentityrelationships.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // CREATE USER
    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        if (userRepository.existsByMobileNumber(
                request.getMobileNumber())) {

            throw new DuplicateResourceException(
                    "Mobile number already exists"
            );
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());

        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            user.setPasswordHash(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole().name());
        } else {
            user.setRole("USER");
        }

        if (request.getStatus() != null
                && !request.getStatus().isBlank()) {

            user.setStatus(request.getStatus());

        } else {
            user.setStatus("ACTIVE");
        }

        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    // GET USER BY ID
    public UserResponse getUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        return convertToResponse(user);
    }

    // GET USER BY ID
    public UserResponse getUserById(Long id) {
        return getUser(id);
    }

    // GET ALL USERS
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // UPDATE USER
    public UserResponse updateUser(
            Long id,
            UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        // Check email duplicate
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(
                request.getEmail())) {

            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        // Check mobile duplicate
        if (request.getMobileNumber() != null
                && !request.getMobileNumber()
                .equals(user.getMobileNumber())
                && userRepository.existsByMobileNumber(
                request.getMobileNumber())) {

            throw new DuplicateResourceException(
                    "Mobile number already exists"
            );
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());

        // Update password only when provided
        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            user.setPasswordHash(
                    passwordEncoder.encode(
                            request.getPassword()
                    )
            );
        }

        // Update role
        if (request.getRole() != null) {
            user.setRole(request.getRole().name());
        }

        // Update status
        if (request.getStatus() != null
                && !request.getStatus().isBlank()) {

            user.setStatus(request.getStatus());
        }

        User updatedUser = userRepository.save(user);

        return convertToResponse(updatedUser);
    }

    // DELETE USER
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        userRepository.delete(user);
    }

    // GET TOP CUSTOMERS
    public List<TopCustomerResponse> getTopCustomers() {

        return userRepository.findTopCustomers();
    }

    // CONVERT USER TO RESPONSE
    private UserResponse convertToResponse(User user) {

        Role role = null;

        if (user.getRole() != null) {
            role = Role.valueOf(user.getRole());
        }

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getStatus(),
                role
        );
    }
}
