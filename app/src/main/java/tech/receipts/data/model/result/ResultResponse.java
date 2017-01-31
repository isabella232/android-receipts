package tech.receipts.data.model.result;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Lottery results data
 **/
public class ResultResponse implements Serializable {

    private String name = null;
    private String date = null;
    private StatusEnum status = null;
    private List<ResultPrizeResponse> prizes = Collections.emptyList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public List<ResultPrizeResponse> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<ResultPrizeResponse> prizes) {
        this.prizes = prizes;
    }
}
