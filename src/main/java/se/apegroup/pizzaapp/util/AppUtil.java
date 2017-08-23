package se.apegroup.pizzaapp.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class AppUtil {

    public static Date getDateFromLocalDateTime(LocalDateTime localDateTime) {
        return new Date(localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    public static LocalDateTime getLocalDateTimeFromDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
