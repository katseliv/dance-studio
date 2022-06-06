package com.dataart.dancestudio.validator;

import com.dataart.dancestudio.annotation.LessonStartDatetimeValid;
import com.dataart.dancestudio.model.dto.LessonDto;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class LessonStartDatetimeValidator implements ConstraintValidator<LessonStartDatetimeValid, LessonDto> {

    @Value("${datetime.limitDays}")
    private int limitDays;

    @Override
    public boolean isValid(final LessonDto lessonDto, final ConstraintValidatorContext context) {
        final List<String> messages = new ArrayList<>();
        if (lessonDto.getStartDatetime() == null) {
            buildConstraintValidatorContext(context, "Datetime is null.");
            return false;
        }

        if (lessonDto.getStartDatetime().isBlank()) {
            messages.add("Datetime is blank.");
        }

        try {
            final LocalDateTime localDateTime = LocalDateTime.parse(lessonDto.getStartDatetime());

            if (!new TimeZoneValidator().isValid(lessonDto.getTimeZone(), context)) {
                return false;
            }
            final ZoneId zoneId = ZoneId.of(lessonDto.getTimeZone());
            final ZonedDateTime localDateTimeZoned = localDateTime.atZone(zoneId);
            final Instant datetimeInstant = localDateTimeZoned.toInstant();

            final LocalDateTime now = LocalDateTime.now().plusDays(limitDays);
            final ZonedDateTime nowZoned = now.atZone(ZoneId.systemDefault());
            final Instant nowInstant = nowZoned.toInstant();

            if (datetimeInstant.isBefore(Instant.now())) {
                messages.add("Datetime is in the past.");
            }

            if (datetimeInstant.isBefore(nowInstant)) {
                messages.add("Datetime must be no later than " + limitDays + " day before the event.");
            }
        } catch (final DateTimeParseException dateTimeParseException) {
            messages.add("Datetime is invalid.");
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
