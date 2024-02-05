package org.kharitonov.person_security.services;

import org.kharitonov.person_security.models.ROLES;
import org.kharitonov.person_security.models.User;
import org.kharitonov.person_security.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Kharitonov Pavel on 04.02.2024.
 * makeAdmin( username ) change role of requested user to Admin
 */
@Service
public class AdminService {

    private final UserRepository repository;

    public AdminService(UserRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> makeAdmin(String username) {
        Optional<User> user = repository.findByUsername(username);
        if (user.isPresent()) {
            User updateUser = new User(user.get().getId(),
                    username,
                    user.get().getPassword(),
                    ROLES.ROLE_ADMIN
            );
            repository.save(updateUser);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
