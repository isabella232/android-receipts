package tech.receipts.components.validation.condition;

import tech.receipts.R;
import tech.receipts.components.validation.Condition;

public class SelectedCondition implements Condition<Boolean> {

    @Override
    public boolean isMet(Boolean value) {
        return value;
    }

    @Override
    public int getErrorMsgResId() {
        return R.string.condition_msg_selected;
    }
}
