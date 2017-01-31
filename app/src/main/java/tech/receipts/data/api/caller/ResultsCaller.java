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
import tech.receipts.data.model.result.LotteryResultsCollectionResponse;
import tech.receipts.ui.view.ResultsMvpView;

public class ResultsCaller extends BaseCaller<ResultsMvpView> {

    @Inject
    public ResultsCaller(
            NetworkStateManager networkStateManager,
            DataManager dataManager,
            Retrofit retrofit
    ) {
        super(networkStateManager, dataManager, retrofit);
    }

    public void loadResults() {
        resetRetry();
        checkViewAttached();
        preCall();
    }

    @Override
    public Subscription call() {
        return getDataManager().getResults()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .timeout(ApplicationConfiguration.RESULTS_CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (isViewAttached()) {
                            getMvpView().startedLoadingData();
                        }
                    }
                })
                .subscribe(new Subscriber<LotteryResultsCollectionResponse>() {
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
                    public void onNext(LotteryResultsCollectionResponse resultsCollectionResponse) {
                        if (isViewAttached()) {
                            getMvpView().onGetResultsFinished(resultsCollectionResponse);
                        }
                    }
                });
    }

}
