package org.kharitonov.person_security.models;

import jakarta.validation.constraints.NotBlank;
import org.kharitonov.person_security.validation.Username;

public record UserDto(

        @Username
        String username,
        @NotBlank
        String password,
        String role) {
}
