package tech.receipts.components;

import org.apache.commons.validator.routines.BigDecimalValidator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import tech.receipts.data.model.ticket.AmountResponse;
import timber.log.Timber;

public class PriceFormatter {
    public static final Locale LOCALE = new Locale("pl", "PL");
    public static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(LOCALE);

    public static String formatAmount(AmountResponse amountResponse) {
        if (amountResponse != null) {
            String value = amountResponse.getValue();
            try {
                value = value.replace(".", ",");
                BigDecimal amount = BigDecimalValidator.getInstance().validate(value);
                if (amount != null) {
                    return CURRENCY_FORMATTER.format(amount.doubleValue());
                }
            } catch (IllegalArgumentException e) {
                Timber.e(e, "Unable to format price: %s", amountResponse);
            }
        }

        return null;
    }
}
