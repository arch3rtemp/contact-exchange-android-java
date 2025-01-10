package dev.arch3rtemp.ui.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.time.ZoneId;
import java.util.Locale;

public class TimeConverterTest {

    private final TimeConverter timeConverter = new TimeConverter();

    @Test
    public void convertLongToDateString() {
        var expected = "06 Jan 25";
        var result = timeConverter.convertLongToDateString(SIMULATED_CREATED_TIME, DATE_PATTERN, Locale.getDefault(), ZoneId.systemDefault());
        assertEquals(expected, result);
    }

    private final static String DATE_PATTERN = "dd MMM yy";
    private final static long SIMULATED_CREATED_TIME = 1736190485327L;
}
