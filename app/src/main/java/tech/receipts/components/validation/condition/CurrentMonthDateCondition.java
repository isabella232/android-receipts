package tech.receipts.components.validation.condition;

import org.apache.commons.validator.routines.DateValidator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Date;

import tech.receipts.components.DateFormatter;
import tech.receipts.R;
import tech.receipts.components.validation.Condition;

public class CurrentMonthDateCondition implements Condition<String> {

    @Override
    public boolean isMet(String value) {
        Date date = DateValidator.getInstance().validate(value);

        if (date == null) {
            return false;
        }

        DateTime now = DateTime.now();
        int currentMonth = now.getMonthOfYear();
        int currentYear = now.getYear();

        try {
            LocalDate dateTime = LocalDate.parse(value, DateFormatter.DATE_FORMATTER);
            return dateTime != null && dateTime.getYear() == currentYear && dateTime.getMonthOfYear() == currentMonth;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    public int getErrorMsgResId() {
        return R.string.condition_msg_date_current_month;
    }
}
