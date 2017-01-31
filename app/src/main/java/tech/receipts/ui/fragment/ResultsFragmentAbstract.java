package tech.receipts.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import tech.receipts.data.model.result.LotteryResultsCollectionResponse;
import timber.log.Timber;

public abstract class ResultsFragmentAbstract extends Fragment {
    protected static final String ARG_COLLECTION = "collection";
    protected static final int ANIMATOR_LIST = 0;
    protected static final int ANIMATOR_EMPTY_LABEL = 1;
    protected LotteryResultsCollectionResponse collectionResponse = new LotteryResultsCollectionResponse();

    public void setCollectionResponse(LotteryResultsCollectionResponse collectionResponse) {
        this.collectionResponse = collectionResponse;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Timber.d("onSaveInstanceState: %s", savedInstanceState);

        if (savedInstanceState != null) {
            Timber.d("onSaveInstanceState save: %d", collectionResponse.getTotal());
            savedInstanceState.putSerializable(ARG_COLLECTION, collectionResponse);
        }

        super.onSaveInstanceState(savedInstanceState);
    }
}
