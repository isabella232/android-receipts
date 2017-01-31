package tech.receipts.data.model.result;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Lottery results collection
 **/
public class LotteryResultsCollectionResponse implements Serializable{

    private Integer total = 0;
    private List<ResultResponse> results = Collections.emptyList();

    public LotteryResultsCollectionResponse() {
    }

    public LotteryResultsCollectionResponse(Integer total, List<ResultResponse> results) {
        this.total = total;
        this.results = results;
    }

    public Integer getTotal() {
        return total;
    }

    public List<ResultResponse> getResults() {
        return results;
    }
}
