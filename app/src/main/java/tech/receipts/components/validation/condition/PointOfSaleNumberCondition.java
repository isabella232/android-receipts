package tech.receipts.components.validation.condition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tech.receipts.R;
import tech.receipts.components.validation.Condition;

public class PointOfSaleNumberCondition implements Condition<String> {
    private static final Collection<Pattern> PATTERNS = new ArrayList<Pattern>() {{
        add(Pattern.compile("^[a-zA-Z]{3}\\d{8}(\\d{2})?$"));
        add(Pattern.compile("^[a-zA-Z]{2}\\d{8}$"));
    }};

    @Override
    public boolean isMet(String value) {
        for (Pattern pattern : PATTERNS) {
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public int getErrorMsgResId() {
        return R.string.condition_msg_pos_number;
    }
}
