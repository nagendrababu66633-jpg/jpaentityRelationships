
package org.example.jpaentityrelationships.dto;

public class AddressResponse {

    private Long id;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private Long userId;

    public AddressResponse(
            Long id,
            String street,
            String city,
            String state,
            String postalCode,
            String country,
            Long userId) {

        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public Long getUserId() {
        return userId;
    }
}
