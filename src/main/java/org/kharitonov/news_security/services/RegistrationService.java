package org.kharitonov.news_security.services;

import org.kharitonov.news_security.mappers.UserMapper;
import org.kharitonov.news_security.models.User;
import org.kharitonov.news_security.models.UserDto;
import org.kharitonov.news_security.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Kharitonov Pavel on 03.02.2024.
 */
@Service
public class RegistrationService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;


    public RegistrationService(UserRepository repository,
                               UserMapper mapper,
                               PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }


    public void addPerson(UserDto dto) {
        User user = mapper.dtoToUser(dto);
        user.setPassword(passwordEncoder
                .encode(user.getPassword()
                ));

        repository.save(user);
    }
}
