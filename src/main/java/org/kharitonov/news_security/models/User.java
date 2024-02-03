package org.kharitonov.news_security.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author Kharitonov Pavel on 30.01.2024.
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "t_user")
@Component
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 2, max = 100, message = "User name must be from 2 to 100 chars length")
    @Column(name = "username")
    private String username;

    @NotBlank
    @Column(name = "password")
    private String password;

    @ElementCollection(targetClass =  ROLES.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<ROLES> roles;


}

