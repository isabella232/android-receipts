package tech.receipts.components.validation.condition;

import org.apache.commons.validator.routines.BigDecimalValidator;

import java.math.BigDecimal;

import tech.receipts.R;
import tech.receipts.components.validation.Condition;

public class AmountNumberCondition implements Condition<String> {

    @Override
    public boolean isMet(String value) {
        value = value.replace(".", ",");
        BigDecimal amount = BigDecimalValidator.getInstance().validate(value);
        return (amount != null && amount.doubleValue() > 0.0);
    }

    @Override
    public int getErrorMsgResId() {
        return R.string.condition_msg_amount;
    }
}
