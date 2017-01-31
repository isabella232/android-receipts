package tech.receipts.data.api.caller;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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
import tech.receipts.data.model.ticket.AddTicketRequest;
import tech.receipts.data.model.ticket.TicketResponse;
import tech.receipts.ui.view.CreateTicketMvpView;

public class CreateTicketCaller extends BaseCaller<CreateTicketMvpView> {

    private AddTicketRequest request;

    @Inject
    public CreateTicketCaller(
            NetworkStateManager networkStateManager,
            DataManager dataManager,
            Retrofit retrofit
    ) {
        super(networkStateManager, dataManager, retrofit);
    }

    public void createTicket(AddTicketRequest request) {
        this.request = request;

        resetRetry();
        checkViewAttached();
        preCall();
    }

    @Override
    public Subscription call() {
        return getDataManager().addTicket(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .timeout(ApplicationConfiguration.CREATE_TICKET_CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (isViewAttached()) {
                            getMvpView().startedLoadingData();
                        }
                    }
                })
                .subscribe(new Subscriber<TicketResponse>() {
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
                    public void onNext(TicketResponse response) {
                        if (isViewAttached()) {
                            getMvpView().onCreateTicketFinished(request, response);
                        }
                    }
                });
    }

    @Override
    protected void handleForbiddenError(ApiError exception) {
        getMvpView().onAlreadyAddedException(exception);
    }

}