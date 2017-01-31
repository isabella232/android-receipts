package tech.receipts.components.validation.condition;

import tech.receipts.R;
import tech.receipts.components.validation.Condition;

public class TaxRegistrationNumberCondition implements Condition<String> {

    @Override
    public boolean isMet(String value) {
        int nsize = value.length();
        int[] weights = {6, 5, 7, 2, 3, 4, 5, 6, 7};
        int j = 0, sum = 0, control = 0;
        int csum = Integer.parseInt(value.substring(nsize - 1));
        if (csum == 0) {
            csum = 10;
        }
        for (int i = 0; i < nsize - 1; i++) {
            char c = value.charAt(i);
            j = Integer.parseInt(String.valueOf(c));
            sum += j * weights[i];
        }
        control = sum % 11;
        return (control == csum);
    }

    @Override
    public int getErrorMsgResId() {
        return R.string.condition_msg_tax_registration_number;
    }
}
