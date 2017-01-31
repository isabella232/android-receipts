package tech.receipts.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import tech.receipts.data.api.ReceiptsApiService;
import tech.receipts.data.api.auth.OnAuthCallback;
import tech.receipts.data.database.AutocompleteDb;
import tech.receipts.data.model.autocomplete.Autocomplete;
import tech.receipts.data.model.result.LotteryResultsCollectionResponse;
import tech.receipts.data.model.ticket.AddTicketRequest;
import tech.receipts.data.model.ticket.LotteryTicketsCollectionResponse;
import tech.receipts.data.model.ticket.TicketResponse;
import tech.receipts.data.model.trade.LotteryTradesCollectionResponse;

@Singleton
public class DataManager {

    private final ReceiptsApiService receiptsApiService;
    private final AutocompleteDb autocompleteDb;
    private final AuthProvider authProvider;

    @Inject
    public DataManager(
            ReceiptsApiService receiptsApiService,
            AutocompleteDb autocompleteDb,
            AuthProvider authProvider
    ) {
        this.receiptsApiService = receiptsApiService;
        this.autocompleteDb = autocompleteDb;
        this.authProvider = authProvider;
    }

    public Observable<TicketResponse> addTicket(AddTicketRequest request) {
        return receiptsApiService.addTicket(request);
    }

    public Observable<LotteryTicketsCollectionResponse> getTickets(
            String userAuth,
            Long limit,
            Long offset,
            String sort
    ) {
        return receiptsApiService.getTickets(userAuth, limit, offset, sort);
    }

    public Observable<LotteryResultsCollectionResponse> getResults() {
        return receiptsApiService.getResults();
    }

    public Observable<LotteryTradesCollectionResponse> getTrades() {
        return receiptsApiService.getTrades();
    }

    public void getNewSession(OnAuthCallback onFinishedListener) {
        authProvider.getNewSession(onFinishedListener);
    }

    public Observable<List<String>> getTaxCollection() {
        return autocompleteDb.getTaxCollection();
    }

    public Observable<List<Autocomplete>> getPosCollection() {
        return autocompleteDb.getPosCollection();
    }

    public Observable<Autocomplete> saveTaxAndPos(Autocomplete autocomplete) {
        return autocompleteDb.saveTaxAndPos(autocomplete);
    }
}
