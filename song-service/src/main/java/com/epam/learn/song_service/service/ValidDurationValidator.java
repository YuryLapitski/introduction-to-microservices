package com.epam.learn.song_service.service;

import com.epam.learn.song_service.service.validator.ValidDuration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDurationValidator implements ConstraintValidator<ValidDuration, String> {

    @Override
    public boolean isValid(String duration, ConstraintValidatorContext context) {
        if (duration == null || !duration.contains(":")) {
            return false;
        }
        String[] parts = duration.split(":");
        if (parts.length < 2 || parts.length > 3) {
            return false;
        }
        try {
            if (parts.length == 2){
                int minutes = Integer.parseInt(parts[0]);
                int seconds = Integer.parseInt(parts[1]);
                return minutes >= 0 && minutes < 60 && seconds >= 0 && seconds < 60;
            } else {
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                int seconds = Integer.parseInt(parts[2]);
                return hours >= 0 && hours < 60 && minutes >= 0 && minutes < 60 && seconds >= 0 && seconds < 60;
            }
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
