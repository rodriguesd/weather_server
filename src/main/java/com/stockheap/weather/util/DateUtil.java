package com.stockheap.weather.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private DateUtil() {

    }

    public static String convertToLocalTime(long epochSeconds, Long timezone) {
        Instant instant = Instant.ofEpochSecond(epochSeconds);
        ZoneOffset offset = ZoneOffset.ofTotalSeconds(timezone.intValue());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, offset);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }


}
