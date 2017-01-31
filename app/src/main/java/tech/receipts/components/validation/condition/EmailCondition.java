package tech.receipts.components.validation.condition;

import org.apache.commons.validator.routines.EmailValidator;

import tech.receipts.R;
import tech.receipts.components.validation.Condition;

public class EmailCondition implements Condition<String> {

    @Override
    public boolean isMet(String value) {
        return EmailValidator.getInstance().isValid(value);
    }

    @Override
    public int getErrorMsgResId() {
        return R.string.condition_msg_email;
    }
}
