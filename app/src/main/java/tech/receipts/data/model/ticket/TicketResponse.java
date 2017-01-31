package tech.receipts.data.model.ticket;

import java.io.Serializable;

public class TicketResponse implements Serializable {
    private String id;
    private String code;
    private String purchaseOrderNumber;
    private AmountResponse amount;
    private String date;

    public TicketResponse() {
    }

    public TicketResponse(String id, String code, String purchaseOrderNumber, AmountResponse amount, String date) {
        this.id = id;
        this.code = code;
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.amount = amount;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public AmountResponse getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
