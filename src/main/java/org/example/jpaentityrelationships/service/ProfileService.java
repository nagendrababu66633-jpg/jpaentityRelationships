package org.example.jpaentityrelationships.service;

import org.example.jpaentityrelationships.dto.ProfileRequest;
import org.example.jpaentityrelationships.dto.ProfileResponse;
import org.example.jpaentityrelationships.entity.Profile;
import org.example.jpaentityrelationships.entity.User;
import org.example.jpaentityrelationships.repository.ProfileRepository;
import org.example.jpaentityrelationships.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileService(
            ProfileRepository profileRepository,
            UserRepository userRepository) {

        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    // CREATE PROFILE
    public ProfileResponse createProfile(
            Long userId,
            ProfileRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + userId
                        )
                );

        Profile profile = new Profile();

        profile.setBio(request.getBio());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setGender(request.getGender());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setUser(user);

        Profile savedProfile =
                profileRepository.save(profile);

        return convertToResponse(savedProfile);
    }


    // GET PROFILE
    public ProfileResponse getProfile(Long userId) {

        Profile profile =
                profileRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profile not found for user id: "
                                                + userId
                                )
                        );

        return convertToResponse(profile);
    }


    // UPDATE PROFILE
    public ProfileResponse updateProfile(
            Long userId,
            ProfileRequest request) {

        Profile profile =
                profileRepository.findByUserId(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Profile not found for user id: "
                                                + userId
                                )
                        );

        profile.setBio(request.getBio());
        profile.setPhoneNumber(request.getPhoneNumber());
        profile.setGender(request.getGender());
        profile.setDateOfBirth(request.getDateOfBirth());

        Profile updatedProfile =
                profileRepository.save(profile);

        return convertToResponse(updatedProfile);
    }


    // CONVERT ENTITY → RESPONSE
    private ProfileResponse convertToResponse(Profile profile) {

        return new ProfileResponse(
                profile.getId(),
                profile.getBio(),
                profile.getPhoneNumber(),
                profile.getGender(),
                profile.getDateOfBirth(),
                profile.getUser().getId()
        );
    }
}