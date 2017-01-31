package tech.receipts.components.validation.condition;

import org.apache.commons.validator.routines.DateValidator;
import org.joda.time.LocalDate;

import java.util.Date;

import tech.receipts.components.DateFormatter;
import tech.receipts.R;
import tech.receipts.components.validation.Condition;

public class DateCondition implements Condition<String> {

    @Override
    public boolean isMet(String value) {
        Date date = DateValidator.getInstance().validate(value);

        if (date == null) {
            return false;
        }

        try {
            LocalDate.parse(value, DateFormatter.DATE_FORMATTER);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    public int getErrorMsgResId() {
        return R.string.condition_msg_date;
    }
}
