package tech.receipts.data.api.caller;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.Credentials;
import retrofit2.Retrofit;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import tech.receipts.components.NetworkStateManager;
import tech.receipts.config.ApplicationConfiguration;
import tech.receipts.data.DataManager;
import tech.receipts.data.model.ApiError;
import tech.receipts.data.model.ticket.GetTicketsRequest;
import tech.receipts.data.model.ticket.LotteryTicketsCollectionResponse;
import tech.receipts.ui.view.TicketsMvpView;

public class TicketsCaller extends BaseCaller<TicketsMvpView> {
    private GetTicketsRequest request;

    @Inject
    public TicketsCaller(
            NetworkStateManager networkStateManager,
            DataManager dataManager,
            Retrofit retrofit
    ) {
        super(networkStateManager, dataManager, retrofit);
    }

    public void loadTickets(GetTicketsRequest request) {
        this.request = request;
        resetRetry();
        checkViewAttached();
        preCall();
    }

    @Override
    public Subscription call() {
        String userAuth = buildUserAuth(request.getEmail(), request.getPassword());

        return getDataManager().getTickets(
                userAuth,
                request.getLimit(),
                request.getOffset(),
                request.getSort())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .timeout(ApplicationConfiguration.GET_TICKETS_CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (isViewAttached()) {
                            getMvpView().startedLoadingData();
                        }
                    }
                })
                .subscribe(new Subscriber<LotteryTicketsCollectionResponse>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getMvpView().finishedLoadingData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        handleApiError(e);
                    }

                    @Override
                    public void onNext(LotteryTicketsCollectionResponse resultsCollectionResponse) {
                        if (isViewAttached()) {
                            getMvpView().onGetTicketsFinished(request, resultsCollectionResponse);
                        }
                    }
                });
    }

    private String buildUserAuth(String email, String password) {
        if (email != null && password != null) {
            return Credentials.basic(email, password).replace("Basic ", "");
        }

        return null;
    }

    @Override
    protected void handleForbiddenError(ApiError exception) {
        if (isViewAttached()) {
            getMvpView().onInvalidUserAuth(exception);
        }
    }
}