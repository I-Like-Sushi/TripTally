package org.example.eindopdrachtbackend.travel.currencyRates;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = org.example.eindopdrachtbackend.travel.currencyRates.AtLeastOneFieldValidator.class)
@Documented
public @interface AtLeastOneField {
    String message() default "At least one of the specified fields must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] fields();
}

