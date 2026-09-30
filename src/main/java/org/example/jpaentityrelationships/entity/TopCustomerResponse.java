package org.example.jpaentityrelationships.entity;

import java.math.BigDecimal;

public class TopCustomerResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal totalSpent;

    public TopCustomerResponse(
            Long id,
            String firstName,
            String lastName,
            String email,
            BigDecimal totalSpent) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.totalSpent = totalSpent;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }
}