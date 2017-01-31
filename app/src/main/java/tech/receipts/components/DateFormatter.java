package tech.receipts.components;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateFormatter {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd.MM.yyyy");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("dd.MM.yyyy HH:mm");

    public static String formatDateFromApi(String dateFromApi) {
        if (dateFromApi != null) {
            DateTime dateTime = new DateTime(dateFromApi);

            LocalDate localDate = dateTime.toLocalDate();
            return DATE_FORMATTER.print(localDate);
        }

        return null;
    }

    public static String formatDateTimeFromApi(String dateFromApi) {
        if (dateFromApi != null) {
            DateTime dateTime = new DateTime(dateFromApi);

            LocalDateTime localDate = dateTime.toLocalDateTime();
            return DATE_TIME_FORMATTER.print(localDate);
        }

        return null;
    }

    public static String formatDateToApi(String dateStr) {
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, DATE_FORMATTER);
        return dateTime.toString();
    }
}
