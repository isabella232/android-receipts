package tech.receipts.data.model.trade;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Lottery trades collection
 **/
public class LotteryTradesCollectionResponse implements Serializable {

    private Integer total = 0;
    private List<TradeResponse> trades = Collections.emptyList();


    public LotteryTradesCollectionResponse() {
    }

    public LotteryTradesCollectionResponse(Integer total, List<TradeResponse> trades) {
        this.total = total;
        this.trades = trades;
    }

    public Integer getTotal() {
        return total;
    }

    public List<TradeResponse> getTrades() {
        return trades;
    }
}
