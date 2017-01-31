package tech.receipts.components.validation;

public class Issue {

    public enum Field {
        TAX_REGISTRATION_NUMBER,
        POINT_OF_SALE,
        PURCHASE_ORDER_NUMBER,
        AMOUNT,
        TRADE,
        DATE,
        USER_PHONE,
        USER_EMAIL,
        TERMS_OF_SERVICE,
        PERSONAL_DATA_PROCESSING,
        USE_MY_EFFIGY
    }

    private final String detailMessage;
    private final Field field;

    public Issue(String detailMessage, Field field) {
        this.detailMessage = detailMessage;
        this.field = field;
    }

    public Field getField() {
        return field;
    }

    public String getDetailMessage() {
        return detailMessage;
    }
}
