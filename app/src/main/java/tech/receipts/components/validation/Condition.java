package tech.receipts.components.validation;

public interface Condition<T> {

    boolean isMet(T value);

    int getErrorMsgResId();

}
