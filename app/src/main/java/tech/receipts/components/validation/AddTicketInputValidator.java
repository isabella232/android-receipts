package tech.receipts.components.validation;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Collection;

import tech.receipts.exception.TicketValidationException;
import tech.receipts.components.validation.Issue.Field;
import tech.receipts.components.validation.condition.AmountNumberCondition;
import tech.receipts.components.validation.condition.AmountValueCondition;
import tech.receipts.components.validation.condition.CurrentMonthDateCondition;
import tech.receipts.components.validation.condition.DateCondition;
import tech.receipts.components.validation.condition.EmailCondition;
import tech.receipts.components.validation.condition.NotEmptyCondition;
import tech.receipts.components.validation.condition.NotEmptySelectionCondition;
import tech.receipts.components.validation.condition.PhoneNumberCondition;
import tech.receipts.components.validation.condition.PointOfSaleNumberCondition;
import tech.receipts.components.validation.condition.PolandPhoneNumberCondition;
import tech.receipts.components.validation.condition.SelectedCondition;
import tech.receipts.components.validation.condition.TaxRegistrationLengthCondition;

public class AddTicketInputValidator {

    private final Resources resources;

    private final Collection<Issue> issues = new ArrayList<>();

    public AddTicketInputValidator(Context context) {
        this.resources = context.getResources();
    }

    public void validate(
            String taxRegistrationNumber,
            String pointOfSale,
            String purchaseOrderNumber,
            String amount,
            String trade,
            String date,
            String userPhone,
            String userEmail,
            Boolean termsOfService,
            Boolean personalDataProcessing,
            Boolean useMyEffigy
    ) throws TicketValidationException {
        issues.clear();

        check(
                Field.TAX_REGISTRATION_NUMBER,
                taxRegistrationNumber,
                new NotEmptyCondition(),
                new TaxRegistrationLengthCondition()
        );
        check(
                Field.POINT_OF_SALE,
                pointOfSale,
                new NotEmptyCondition(),
                new PointOfSaleNumberCondition()
        );
        check(Field.PURCHASE_ORDER_NUMBER, purchaseOrderNumber, new NotEmptyCondition());
        check(
                Field.AMOUNT,
                amount,
                new NotEmptyCondition(),
                new AmountNumberCondition(),
                new AmountValueCondition()
        );
        check(Field.TRADE, trade, new NotEmptySelectionCondition());
        check(
                Field.DATE,
                date,
                new NotEmptyCondition(),
                new DateCondition(),
                new CurrentMonthDateCondition()
        );
        check(
                Field.USER_PHONE,
                userPhone,
                new NotEmptyCondition(),
                new PolandPhoneNumberCondition(),
                new PhoneNumberCondition()
        );
        check(
                Field.USER_EMAIL,
                userEmail,
                new NotEmptyCondition(),
                new EmailCondition()
        );
        check(Field.TERMS_OF_SERVICE, termsOfService, new SelectedCondition());
        check(Field.PERSONAL_DATA_PROCESSING, personalDataProcessing, new SelectedCondition());
//        check(Field.USE_MY_EFFIGY, useMyEffigy, new SelectedCondition());

        if (!issues.isEmpty()) {
            throw new TicketValidationException(issues);
        }
    }

    private void check(Field field, String value, Condition... conditions) {
        for (Condition condition : conditions) {
            if (!condition.isMet(value)) {
                issues.add(new Issue(resources.getString(condition.getErrorMsgResId()), field));
                break;
            }

        }
    }

    private void check(Field field, Boolean value, Condition... conditions) {
        for (Condition condition : conditions) {
            if (!condition.isMet(value)) {
                issues.add(new Issue(resources.getString(condition.getErrorMsgResId()), field));
                break;
            }

        }
    }
}
