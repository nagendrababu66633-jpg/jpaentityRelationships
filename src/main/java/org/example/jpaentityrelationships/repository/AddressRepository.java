 package org.example.jpaentityrelationships.repository;

import org.example.jpaentityrelationships.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository
        extends JpaRepository<Address, Long> {

    List<Address> findByUserId(Long userId);
}

