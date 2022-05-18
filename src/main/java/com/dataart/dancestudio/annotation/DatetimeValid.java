package com.dataart.dancestudio.annotation;

import com.dataart.dancestudio.validator.DatetimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DatetimeValidator.class)
public @interface DatetimeValid {

    String message() default "Datetime invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
