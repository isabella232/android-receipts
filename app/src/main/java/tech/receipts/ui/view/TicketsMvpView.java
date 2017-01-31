package tech.receipts.ui.view;

import tech.receipts.data.model.ApiError;
import tech.receipts.data.model.ticket.GetTicketsRequest;
import tech.receipts.data.model.ticket.LotteryTicketsCollectionResponse;

public interface TicketsMvpView extends MvpView {

    void onGetTicketsFinished(GetTicketsRequest request, LotteryTicketsCollectionResponse lotteryTicketsCollectionResponse);

    void onInvalidUserAuth(ApiError error);
}
