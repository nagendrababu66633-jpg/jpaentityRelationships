package org.example.jpaentityrelationships.service;


import org.example.jpaentityrelationships.dto.UserRequest;
import org.example.jpaentityrelationships.dto.UserResponse;
import org.example.jpaentityrelationships.entity.User;
import org.example.jpaentityrelationships.exceptions.DuplicateResourceException;
import org.example.jpaentityrelationships.exceptions.ResourceNotFoundException;
import org.example.jpaentityrelationships.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new DuplicateResourceException(
                    "Mobile number already exists"
            );
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        user.setStatus(request.getStatus());
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }


    public UserResponse getUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        return toResponse(user);
    }


    private UserResponse toResponse(User user) {

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getStatus(),
                user.getRole());
    }
}
