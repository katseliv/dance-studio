package com.dataart.dancestudio.utils;

import com.dataart.dancestudio.exception.ParseInputException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Slf4j
public class ParseUtils {

    public static Integer parsePositiveInteger(final String value) {
        final int intValue;
        try {
            intValue = Integer.parseInt(value);
        } catch (final NumberFormatException exception) {
            log.error("Can't parse input value {}", value);
            throw new ParseInputException("Invalid input string. Can't parse to number.", exception);
        }

        if (intValue <= 0) {
            log.error("Input value {} isn't positive number.", intValue);
            throw new ParseInputException("Number isn't positive.");
        }
        return intValue;
    }

    public static LocalDate parseDateString(final String value) {
        try {
            return LocalDate.parse(value);
        } catch (final DateTimeParseException exception) {
            log.error("Can't parse input value {}", value);
            throw new ParseInputException("Invalid input string. Can't parse to date.");
        }
    }

}
