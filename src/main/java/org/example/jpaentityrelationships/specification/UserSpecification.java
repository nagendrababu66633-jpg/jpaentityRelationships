package org.example.jpaentityrelationships.specification;


import org.example.jpaentityrelationships.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> firstNameContains(
            String firstName
    ) {

        return (root, query, cb) -> {

            if (firstName == null || firstName.isBlank()) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get("firstName")),
                    "%" + firstName.toLowerCase() + "%"
            );
        };
    }


    public static Specification<User> emailContains(
            String email
    ) {

        return (root, query, cb) -> {

            if (email == null || email.isBlank()) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get("email")),
                    "%" + email.toLowerCase() + "%"
            );
        };
    }


    public static Specification<User> status(
            String status
    ) {

        return (root, query, cb) -> {

            if (status == null || status.isBlank()) {
                return null;
            }

            return cb.equal(
                    root.get("status"),
                    status
            );
        };
    }
}
