package org.kharitonov.person_security.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UsernameValidator.class)
@Documented
public @interface Username {
    String message() default "Username is already taken or empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
