package org.kharitonov.news_security.models;

import jakarta.validation.constraints.NotBlank;
import org.kharitonov.news_security.validation.Username;

public record UserDto(

        @Username
        String username,
        @NotBlank
        String password,
        String roles) {
}
