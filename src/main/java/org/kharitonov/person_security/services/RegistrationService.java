package org.kharitonov.person_security.services;

import org.kharitonov.person_security.mappers.UserMapper;
import org.kharitonov.person_security.models.User;
import org.kharitonov.person_security.models.UserDto;
import org.kharitonov.person_security.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Kharitonov Pavel on 03.02.2024.
 * addPerson(dto) use mapper to convert data and after that encode password
 */
@Service
public class RegistrationService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            UserRepository repository,
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
