package tech.receipts.components;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import tech.receipts.data.model.result.LotteryResultsCollectionResponse;
import tech.receipts.data.model.result.ResultPrizeResponse;
import tech.receipts.data.model.result.ResultPrizeWinnerResponse;
import tech.receipts.data.model.result.ResultResponse;
import tech.receipts.data.model.ticket.LotteryTicketsCollectionResponse;
import tech.receipts.data.model.ticket.TicketResponse;

public class WinnerCodeCheckComponent {

    @Inject
    public WinnerCodeCheckComponent() {
    }

    public Map<String, String> checkWinTickets(
            LotteryTicketsCollectionResponse tickets,
            LotteryResultsCollectionResponse finishedResults
    ) {
        Map<String, String> userWinnerCodes = new HashMap<>();

        if (tickets != null && tickets.getTotal() > 0) {
            if (finishedResults != null && finishedResults.getTotal() > 0) {
                Map<String, String> winnerCodes = new HashMap<>();


                for (ResultResponse result : finishedResults.getResults()) {
                    for (ResultPrizeResponse prize : result.getPrizes()) {
                        for (ResultPrizeWinnerResponse winner : prize.getWinners()) {
                            winnerCodes.put(winner.getCode(), prize.getName());
//                            Timber.d("Adding winner code: " + winner.getCode() + " : " + prize.getName());
                        }
                    }
                }

                for (TicketResponse ticket : tickets.getTickets()) {
                    String code = ticket.getCode();
//                    Timber.d("Checking code: " + code);

                    if (winnerCodes.containsKey(code)) {
//                        Timber.d("Found winner code: " + code);
                        String prizeName = winnerCodes.get(code);
                        userWinnerCodes.put(code, prizeName);
                    }
                }
            }
        }
        return userWinnerCodes;
    }
}
