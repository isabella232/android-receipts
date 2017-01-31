package tech.receipts.ui.view;

import tech.receipts.data.model.result.LotteryResultsCollectionResponse;

public interface ResultsMvpView extends MvpView {

    void onGetResultsFinished(LotteryResultsCollectionResponse lotteryResultsCollectionResponse);
}
