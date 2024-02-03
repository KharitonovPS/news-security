package org.kharitonov.news_security.services;

import lombok.extern.slf4j.Slf4j;
import org.kharitonov.news_security.models.User;
import org.kharitonov.news_security.repositories.UserRepository;
import org.kharitonov.news_security.security.PersonDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Kharitonov Pavel on 30.01.2024.
 */
@Service
@Slf4j
public class PersonDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public PersonDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(
                    "User, with %s username not found".formatted(username)
            );
        }

        return new PersonDetails(user.get());
    }
}
