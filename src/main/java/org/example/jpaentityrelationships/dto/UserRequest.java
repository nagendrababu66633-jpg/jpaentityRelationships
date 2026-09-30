
        package org.example.jpaentityrelationships.dto;

import org.example.jpaentityrelationships.entity.Role;

public class UserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String password;
    private String status;
    private Role role;

    public UserRequest() {}

    public UserRequest(String firstName,
                       String lastName,
                       String email,
                       String mobileNumber,
                       String password,
                       String status,
                       Role role) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.status = status;
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

