package tech.receipts.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import tech.receipts.R;
import tech.receipts.data.model.trade.LotteryTradesCollectionResponse;
import tech.receipts.data.model.trade.TradeResponse;
import timber.log.Timber;

public class TradesAdapter {

    private final ArrayAdapter<String> adapter;
    private LotteryTradesCollectionResponse collectionResponse;

    public TradesAdapter(Context context) {
        String otherTradeName = context.getResources().getText(R.string.other_enum_translation).toString();
        final TradeResponse defaultTrade = new TradeResponse("OTHER", otherTradeName);
        List<TradeResponse> list = new ArrayList<TradeResponse>() {{
            add(defaultTrade);
        }};

        collectionResponse = new LotteryTradesCollectionResponse(list.size(), list);

        this.adapter = new ArrayAdapter<>(
                context,
                R.layout.trade_dropdown_item,
                itemsFromCollection(collectionResponse)
        );
    }

    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }

    private List<String> itemsFromCollection(LotteryTradesCollectionResponse collectionResponse) {
        List<String> list = new ArrayList<>();

        if (collectionResponse != null && collectionResponse.getTotal() > 0) {
            for (TradeResponse trade : collectionResponse.getTrades()) {
                list.add(trade.getName());
            }
        }

        return list;
    }

    public void updateFromCollection(LotteryTradesCollectionResponse collectionResponse) {
        Timber.d("updateFromCollection: %d", collectionResponse.getTotal());
        this.collectionResponse = collectionResponse;
        adapter.clear();
        adapter.addAll(itemsFromCollection(collectionResponse));
    }

    public String getTradeId(String name) {
        if (collectionResponse != null && collectionResponse.getTotal() > 0) {
            for (TradeResponse trade : collectionResponse.getTrades()) {
                if (trade.getName().equals(name)) {
                    return trade.getId();
                }
            }
        }

        return null;
    }

    public boolean isDefault() {
        return collectionResponse.getTotal() == 1;
    }
}
