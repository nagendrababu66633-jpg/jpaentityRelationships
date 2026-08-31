package org.example.jpaentityrelationships.dto;

import java.time.LocalDate;

public class ProfileResponse {

    private Long id;
    private String bio;
    private String phoneNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private Long userId;

    public ProfileResponse() {
    }

    public ProfileResponse(
            Long id,
            String bio,
            String phoneNumber,
            String gender,
            LocalDate dateOfBirth,
            Long userId) {

        this.id = id;
        this.bio = bio;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}