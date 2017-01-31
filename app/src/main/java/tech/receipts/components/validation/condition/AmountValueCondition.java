package tech.receipts.components.validation.condition;

import org.apache.commons.validator.routines.BigDecimalValidator;

import java.math.BigDecimal;

import tech.receipts.R;
import tech.receipts.components.validation.Condition;

public class AmountValueCondition implements Condition<String> {

    @Override
    public boolean isMet(String value) {
        value = value.replace(".", ",");
        BigDecimal amount = BigDecimalValidator.getInstance().validate(value);
        return BigDecimalValidator.getInstance().minValue(amount, new BigDecimal(10.0));
    }

    @Override
    public int getErrorMsgResId() {
        return R.string.condition_msg_amount_min;
    }
}
