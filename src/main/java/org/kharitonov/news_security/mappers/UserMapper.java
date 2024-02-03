package org.kharitonov.news_security.mappers;

import org.kharitonov.news_security.models.ROLES;
import org.kharitonov.news_security.models.User;
import org.kharitonov.news_security.models.UserDto;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author Kharitonov Pavel on 03.02.2024.
 */
@Component
public class UserMapper {

    public User dtoToUser(UserDto dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        setBasicAuthorities(user);
        return user;
    }

    public UserDto userToDto(User user) {
        return new UserDto(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().toString()
        );
    }


    private void setBasicAuthorities(User user) {
        user.setRoles(Collections.singleton(ROLES.ROLE_USER));
    }
}
