package tech.receipts.data.api.caller;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;
import tech.receipts.components.NetworkStateManager;
import tech.receipts.config.ApplicationConfiguration;
import tech.receipts.data.DataManager;
import tech.receipts.data.api.ErrorUtils;
import tech.receipts.data.model.ApiError;
import tech.receipts.data.model.trade.LotteryTradesCollectionResponse;
import tech.receipts.ui.view.TradesMvpView;
import timber.log.Timber;

public class GetTradesCaller extends BaseCaller<TradesMvpView> {

    @Inject
    public GetTradesCaller(
            NetworkStateManager networkStateManager,
            DataManager dataManager,
            Retrofit retrofit
    ) {
        super(networkStateManager, dataManager, retrofit);
    }

    public void loadTrades() {
        resetRetry();
        checkViewAttached();
        preCall();
    }

    @Override
    public Subscription call() {
        return getDataManager().getTrades()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .timeout(ApplicationConfiguration.TRADES_CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //nop
                    }
                })
                .subscribe(new Subscriber<LotteryTradesCollectionResponse>() {
                    @Override
                    public void onCompleted() {
                        //nop
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an api error");
                        ApiError exception = ApiError.internalApiError();

                        if (e instanceof HttpException) {
                            HttpException ex = (HttpException) e;
                            int code = ex.code();

                            if (ex.response() != null) {
                                exception = ErrorUtils.parseError(ex.response(), getRetrofit());
                            }

                            exception.setStatusCode(code);

                            switch (exception.getStatusCode()) {
                                case UNAUTHORIZED:
                                    authorizeAndRetry();
                                    break;
                                case FORBIDDEN:
                                    handleForbiddenError(exception);
                                    break;
                                default:
                                //nop
                            }
                        }
                    }

                    @Override
                    public void onNext(LotteryTradesCollectionResponse tradesCollectionResponse) {
                        if (isViewAttached()) {
                            getMvpView().onGetTradesFinished(tradesCollectionResponse);
                        }
                    }
                });
    }

}
