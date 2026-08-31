 package org.example.jpaentityrelationships.service;

import org.example.jpaentityrelationships.dto.AddressRequest;
import org.example.jpaentityrelationships.dto.AddressResponse;
import org.example.jpaentityrelationships.entity.Address;
import org.example.jpaentityrelationships.entity.User;
import org.example.jpaentityrelationships.repository.AddressRepository;
import org.example.jpaentityrelationships.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(
            AddressRepository addressRepository,
            UserRepository userRepository) {

        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public AddressResponse createAddress(
            Long userId,
            AddressRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: " + userId
                        )
                );

        Address address = new Address();

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setUser(user);

        Address savedAddress =
                addressRepository.save(address);

        return convertToResponse(savedAddress);
    }

    // GET ALL
    public List<AddressResponse> getAddresses(Long userId) {

        List<Address> addresses =
                addressRepository.findByUserId(userId);

        return addresses.stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET ONE
    public AddressResponse getAddress(Long addressId) {

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Address not found with id: "
                                                + addressId
                                )
                        );

        return convertToResponse(address);
    }

    // UPDATE
    public AddressResponse updateAddress(
            Long addressId,
            AddressRequest request) {

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Address not found with id: "
                                                + addressId
                                )
                        );

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        Address updatedAddress =
                addressRepository.save(address);

        return convertToResponse(updatedAddress);
    }

    // DELETE
    public void deleteAddress(Long addressId) {

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Address not found with id: "
                                                + addressId
                                )
                        );

        addressRepository.delete(address);
    }

    // ENTITY → RESPONSE
    private AddressResponse convertToResponse(
            Address address) {

        return new AddressResponse(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry(),
                address.getUser().getId()
        );
    }
}

