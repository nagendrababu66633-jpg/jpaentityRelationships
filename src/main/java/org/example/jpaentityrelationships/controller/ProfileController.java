package org.example.jpaentityrelationships.controller;

import org.example.jpaentityrelationships.dto.ProfileRequest;
import org.example.jpaentityrelationships.dto.ProfileResponse;
import org.example.jpaentityrelationships.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // CREATE PROFILE
    @PostMapping("/user/{userId}")
    public ResponseEntity<ProfileResponse> createProfile(
            @PathVariable Long userId,
            @RequestBody ProfileRequest request) {

        ProfileResponse response =
                profileService.createProfile(userId, request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // GET PROFILE
    @GetMapping("/user/{userId}")
    public ResponseEntity<ProfileResponse> getProfile(
            @PathVariable Long userId) {

        ProfileResponse response =
                profileService.getProfile(userId);

        return ResponseEntity.ok(response);
    }

    // UPDATE PROFILE
    @PutMapping("/user/{userId}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable Long userId,
            @RequestBody ProfileRequest request) {

        ProfileResponse response =
                profileService.updateProfile(userId, request);

        return ResponseEntity.ok(response);
    }
}