package tech.receipts.components.validation.condition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tech.receipts.R;
import tech.receipts.components.validation.Condition;

public class PolandPhoneNumberCondition implements Condition<String> {
    private static final Pattern POLAND_PHONE_NUMBER = Pattern.compile("^\\+48");

    @Override
    public boolean isMet(String value) {
        Matcher matcher = POLAND_PHONE_NUMBER.matcher(value);
        return matcher.find();
    }

    @Override
    public int getErrorMsgResId() {
        return R.string.condition_msg_phone_poland;
    }
}
