package tech.receipts.data.model.ticket;

import java.io.Serializable;

public class AmountResponse implements Serializable {

    private CurrencyEnum currency;
    private String value;

    public AmountResponse() {
    }

    public AmountResponse(CurrencyEnum currency, String value) {
        this.currency = currency;
        this.value = value;
    }

    public CurrencyEnum getCurrency() {
        return currency;
    }

    public String getValue() {
        return value;
    }
}
