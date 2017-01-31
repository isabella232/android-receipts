package tech.receipts.ui.view;

import tech.receipts.data.model.ApiError;
import tech.receipts.data.model.ticket.AddTicketRequest;
import tech.receipts.data.model.ticket.TicketResponse;

public interface CreateTicketMvpView extends MvpView {

    void onCreateTicketFinished(AddTicketRequest request, TicketResponse ticketResponse);

    void onAlreadyAddedException(ApiError apiError);
}
