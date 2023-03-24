package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DataValidator implements ConstraintValidator<DataConstraint, LocalDate> {

    @Override
    public void initialize(DataConstraint contactNumber) {
    }

    @Override
    public boolean isValid(LocalDate contactField,
                           ConstraintValidatorContext cxt) {
        return contactField.isAfter(LocalDate.of(1895, 12, 28));
    }
}