package tech.receipts.data.model.ticket;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class LotteryTicketsCollectionResponse implements Serializable {

    private Integer total = 0;
    private List<TicketResponse> tickets = Collections.emptyList();

    public LotteryTicketsCollectionResponse() {
    }

    public LotteryTicketsCollectionResponse(Integer total, List<TicketResponse> tickets) {
        this.total = total;
        this.tickets = tickets;
    }

    public Integer getTotal() {
        return total;
    }

    public List<TicketResponse> getTickets() {
        return tickets;
    }
}
