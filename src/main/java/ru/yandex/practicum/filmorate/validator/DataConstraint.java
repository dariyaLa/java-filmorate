package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DataValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataConstraint {
    String message() default "Invalid date release";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
