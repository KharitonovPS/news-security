package org.kharitonov.news_security.mappers;

import org.kharitonov.news_security.models.ROLES;
import org.kharitonov.news_security.models.User;
import org.kharitonov.news_security.models.UserDto;
import org.springframework.stereotype.Component;

/**
 * @author Kharitonov Pavel on 03.02.2024.
 * convert dto to User Entity
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

    private void setBasicAuthorities(User user) {
        user.setRole(ROLES.ROLE_USER);
    }
}
