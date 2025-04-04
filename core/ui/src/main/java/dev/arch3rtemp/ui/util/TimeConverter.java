package dev.arch3rtemp.ui.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.inject.Inject;

public class TimeConverter {

    @Inject
    public TimeConverter() {}

    /**
     * Converts epoch milliseconds to a formatted date string.
     *
     * @param epochMillis The epoch time in milliseconds.
     * @param pattern The date-time pattern, e.g., "yyyy-MM-dd HH:mm:ss".
     * @param locale The locale for formatting.
     * @param zoneId The time zone identifier as a string, e.g., "America/New_York".
     * @return The formatted date string.
     */
    public String convertLongToDateString(long epochMillis, String pattern, Locale locale, ZoneId zoneId) {
        Instant instant = Instant.ofEpochMilli(epochMillis); // Create an Instant from milliseconds
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime(); // Convert to LocalDateTime in the specified time zone
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, locale); // Create a DateTimeFormatter with the specified pattern and locale
        return localDateTime.format(formatter); // Format the LocalDateTime
    }
}
