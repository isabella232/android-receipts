package tech.receipts.components.validation.condition;

import tech.receipts.R;
import tech.receipts.components.validation.Condition;

public class NotEmptyCondition implements Condition<String> {

    @Override
    public boolean isMet(String value) {
        return !value.isEmpty();
    }

    @Override
    public int getErrorMsgResId() {
        return R.string.condition_msg_not_empty;
    }
}
