package dev.arch3rtemp.ui.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import javax.inject.Inject;

public class TimeConverter {

    @Inject
    public TimeConverter() {}

    public String convertLongToDateString(long epochMillis, String pattern, Locale locale, ZoneId zoneId) {
        Instant instant = Instant.ofEpochMilli(epochMillis); // Create an Instant from milliseconds
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime(); // Convert to LocalDateTime in the specified time zone
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale); // Create a DateTimeFormatter with the specified pattern and locale
        return localDateTime.format(formatter); // Format the LocalDateTime
    }

    public Long convertDateStringToLong(String dateString, String pattern, Locale locale, ZoneId zoneId) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale);
            LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);
            ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
            Instant instant = zonedDateTime.toInstant();
            return instant.toEpochMilli();
        } catch (DateTimeParseException e) {
            // Handle parsing errors appropriately (e.g., log the error, throw an exception, return null)
            System.err.println("Error parsing date: " + e.getMessage());
            return null; // Or throw an exception if you prefer
        }
    }
}
