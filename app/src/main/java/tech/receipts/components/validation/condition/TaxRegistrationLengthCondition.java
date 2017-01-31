package tech.receipts.components.validation.condition;

import tech.receipts.R;
import tech.receipts.components.validation.Condition;

public class TaxRegistrationLengthCondition implements Condition<String> {

    @Override
    public boolean isMet(String value) {
        return value.length() == 10;
    }

    @Override
    public int getErrorMsgResId() {
        return R.string.condition_msg_tax_registration_length;
    }
}
