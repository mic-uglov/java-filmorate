package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AfterValidator implements ConstraintValidator<After, LocalDate> {
    private LocalDate dateToCompare;

    @Override
    public void initialize(After annotation) {
        try {
            dateToCompare = LocalDate.parse(annotation.value());
        } catch (DateTimeParseException e) {
            dateToCompare = null;
        }
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (date != null && dateToCompare != null) {
            return date.isAfter(dateToCompare);
        }

        return true;
    }
}