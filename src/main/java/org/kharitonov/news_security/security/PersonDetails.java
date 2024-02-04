package org.kharitonov.news_security.security;

import lombok.Getter;
import lombok.ToString;
import org.kharitonov.news_security.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Kharitonov Pavel on 30.01.2024.
 * impliments UserDetails to add User Entity to security context
 */
@Getter
@Component
@ToString
public class PersonDetails implements UserDetails {

    //Use to get data from authorized user
    private final User user;

    public PersonDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority(user.getRole().toString())
        );
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
