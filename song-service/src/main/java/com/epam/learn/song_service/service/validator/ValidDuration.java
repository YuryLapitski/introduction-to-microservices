package com.epam.learn.song_service.service.validator;

import com.epam.learn.song_service.service.ValidDurationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidDurationValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface ValidDuration {
    String message() default "Invalid duration format.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
