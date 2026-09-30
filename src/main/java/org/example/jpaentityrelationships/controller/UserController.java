package org.example.jpaentityrelationships.controller;


import org.example.jpaentityrelationships.dto.UserRequest;
import org.example.jpaentityrelationships.dto.UserResponse;
import org.example.jpaentityrelationships.entity.TopCustomerResponse;
import org.example.jpaentityrelationships.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CREATE USER
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @RequestBody UserRequest request) {

        UserResponse response =
                userService.createUser(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // GET ALL USERS
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> response =
                userService.getAllUsers();

        return ResponseEntity.ok(response);
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id) {

        UserResponse response =
                userService.getUser(id);

        return ResponseEntity.ok(response);
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest request) {

        UserResponse response =
                userService.updateUser(id, request);

        return ResponseEntity.ok(response);
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    // GET TOP CUSTOMERS
    @GetMapping("/top-customers")
    public ResponseEntity<List<TopCustomerResponse>> getTopCustomers() {

        List<TopCustomerResponse> response =
                userService.getTopCustomers();

        return ResponseEntity.ok(response);
    }
}