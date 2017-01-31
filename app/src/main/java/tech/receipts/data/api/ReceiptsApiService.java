package tech.receipts.data.api;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import tech.receipts.data.model.result.LotteryResultsCollectionResponse;
import tech.receipts.data.model.ticket.AddTicketRequest;
import tech.receipts.data.model.ticket.LotteryTicketsCollectionResponse;
import tech.receipts.data.model.ticket.TicketResponse;
import tech.receipts.data.model.trade.LotteryTradesCollectionResponse;
import tech.receipts.config.ApplicationConfiguration;

public interface ReceiptsApiService {
    public static final String TRADES_RESOURCE = "/api/lotteries/111/trades";
    public static final String TICKETS_RESOURCE = "/api/lotteries/111/tickets";
    public static final String RESULTS_RESOURCE = "/api/lotteries/111/results";

    @POST(TICKETS_RESOURCE)
    @Headers({
            "Accept: " + ApplicationConfiguration.API_VERSION_1
    })
    Observable<TicketResponse> addTicket(@Body AddTicketRequest request);

    @GET(TICKETS_RESOURCE)
    @Headers({
            "Accept: " + ApplicationConfiguration.API_VERSION_1
    })
    Observable<LotteryTicketsCollectionResponse> getTickets(
            @Header("UserAuth") String userAuth,
            @Query("limit") Long limit,
            @Query("offset") Long offset,
            @Query("sort") String sort
    );

    @GET(RESULTS_RESOURCE)
    @Headers({
            "Accept: " + ApplicationConfiguration.API_VERSION_1
    })
    Observable<LotteryResultsCollectionResponse> getResults();

    @GET(TRADES_RESOURCE)
    @Headers({
            "Accept: " + ApplicationConfiguration.API_VERSION_1
    })
    Observable<LotteryTradesCollectionResponse> getTrades();
}
