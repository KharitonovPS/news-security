package org.kharitonov.news_security.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.kharitonov.news_security.models.User;
import org.kharitonov.news_security.repositories.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Kharitonov Pavel on 03.02.2024.
 * return MethodArgumentNotValidEx if username empty, null, numberic or not unique
 */

@Component
public class UsernameValidator implements ConstraintValidator<Username, Object> {
    private final UserRepository repository;

    public UsernameValidator(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void initialize(Username username) {
        ConstraintValidator.super.initialize(username);
    }

    @Override
    public boolean isValid(
            Object input,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        if (input == null || input.equals("")) {
            return false;
        }
        // Numeric string is not allowed
        if (input instanceof String && ((String) input).matches("\\d+")) {
            return false;
        }
        if (input instanceof String && !input.equals("null")) {
            Optional<User> user = repository.findByUsername((String) input);
            return user.isEmpty();
        }
        return false;
    }
}
