 package org.example.jpaentityrelationships.controller;

import org.example.jpaentityrelationships.dto.AddressRequest;
import org.example.jpaentityrelationships.dto.AddressResponse;
import org.example.jpaentityrelationships.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // CREATE
    @PostMapping("/user/{userId}")
    public ResponseEntity<AddressResponse> createAddress(
            @PathVariable Long userId,
            @RequestBody AddressRequest request) {

        return ResponseEntity.ok(
                addressService.createAddress(userId, request)
        );
    }

    // GET ALL ADDRESSES OF USER
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponse>> getAddresses(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                addressService.getAddresses(userId)
        );
    }

    // GET ONE ADDRESS
    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponse> getAddress(
            @PathVariable Long addressId) {

        return ResponseEntity.ok(
                addressService.getAddress(addressId)
        );
    }

    // UPDATE
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressRequest request) {

        return ResponseEntity.ok(
                addressService.updateAddress(
                        addressId,
                        request
                )
        );
    }

    // DELETE
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long addressId) {

        addressService.deleteAddress(addressId);

        return ResponseEntity.noContent().build();
    }
}
