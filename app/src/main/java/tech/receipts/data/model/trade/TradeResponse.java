package tech.receipts.data.model.trade;

/**
 * Lottery trade data
 **/
public class TradeResponse {

    private String id = null;
    private String name = null;

    public TradeResponse() {
    }

    public TradeResponse(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
