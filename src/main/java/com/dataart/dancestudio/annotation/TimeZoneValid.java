package com.dataart.dancestudio.annotation;

import com.dataart.dancestudio.validator.TimeZoneValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeZoneValidator.class)
public @interface TimeZoneValid {

    String message() default "Time Zone is invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
