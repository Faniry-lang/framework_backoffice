package itu.framework.backoffice.helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateFormatHelper {
    public static String formatDate(String dateStr) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(dateStr);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd 'à' HH:mm");
            return dateTime.format(formatter);
        } catch (Exception e) {
            return dateStr;
        }
    }
}
