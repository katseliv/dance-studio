package com.dataart.dancestudio.validator;

import com.dataart.dancestudio.annotation.TimeZoneValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class TimeZoneValidator implements ConstraintValidator<TimeZoneValid, String> {

    @Override
    public boolean isValid(final String timeZone, final ConstraintValidatorContext context) {
        final List<String> messages = new ArrayList<>();
        if (timeZone == null) {
            buildConstraintValidatorContext(context, "Time Zone is null.");
            return false;
        }

        if (timeZone.isBlank()) {
            messages.add("Time Zone is blank.");
        }

        try {
            final ZoneId zoneId = ZoneId.of(timeZone);
        } catch (final DateTimeException dateTimeException) {
            messages.add("Time Zone is invalid.");
        }

        if (messages.size() == 0) {
            return true;
        }

        final String messageTemplate = String.join(", ", messages);
        buildConstraintValidatorContext(context, messageTemplate);
        return false;
    }

    private void buildConstraintValidatorContext(final ConstraintValidatorContext context, final String messageTemplate) {
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
    }

}
