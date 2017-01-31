package tech.receipts.ui.view;

import tech.receipts.data.model.trade.LotteryTradesCollectionResponse;

public interface TradesMvpView extends MvpView {

    void onGetTradesFinished(LotteryTradesCollectionResponse lotteryTradesCollectionResponse);
}
