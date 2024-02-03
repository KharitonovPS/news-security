package org.kharitonov.news_security.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.news_security.models.User;
import org.kharitonov.news_security.repositories.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.TreeSet;

/**
 * @author Kharitonov Pavel on 03.02.2024.
 */

@Component
@Slf4j
public class UsernameValidator implements ConstraintValidator<Username, Object> {
    private final UserRepository repository;

    public UsernameValidator(UserRepository repository, TreeSet<String> inMemoryCodes) {
        this.repository = repository;
    }

    @Override
    public void initialize(Username username) {
        ConstraintValidator.super.initialize(username);
    }

    @Override
    public boolean isValid(Object input,
            ConstraintValidatorContext constraintValidatorContext
    ){
        if (input == null) {
            return false;
        }
        if (input.equals(""))
            return false;

        if (input instanceof Number) {
            return false;
        }
        if (input instanceof String) {
            Optional<User> user = repository.findByUsername((String) input);
            return user.isEmpty();
        }
        return false;
    }
}
