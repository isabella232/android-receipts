package tech.receipts.data.model.result;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ResultPrizeResponse implements Serializable {

    private String name = null;
    private List<ResultPrizeWinnerResponse> winners = Collections.emptyList();

    public String getName() {
        return name;
    }

    public List<ResultPrizeWinnerResponse> getWinners() {
        return winners;
    }
}
