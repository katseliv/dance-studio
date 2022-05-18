package com.dataart.dancestudio.validator;

import com.dataart.dancestudio.annotation.DatetimeValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class DatetimeValidator implements ConstraintValidator<DatetimeValid, String> {

    @Override
    public boolean isValid(final String datetime, final ConstraintValidatorContext context) {
        final LocalDateTime localDateTime = LocalDateTime.parse(datetime);
        final ZonedDateTime localDateTimeZoned = localDateTime.atZone(ZoneId.systemDefault());
        final Instant datetimeInstant = localDateTimeZoned.toInstant();

        final LocalDateTime now = LocalDateTime.now().plusHours(3);
        final ZonedDateTime nowZoned = now.atZone(ZoneId.systemDefault());
        final Instant nowInstant = nowZoned.toInstant();

        final List<String> messages = new ArrayList<>();
        boolean result = true;
        if (datetimeInstant.isBefore(Instant.now())) {
            messages.add("Datetime is in the past.");
            result = false;
        }

        if (datetimeInstant.isBefore(nowInstant)) {
            messages.add("Datetime must be no later than 3 hours before the event.");
            result = false;
        }

        final String messageTemplate = String.join(", ", messages);
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return result;
    }

}
