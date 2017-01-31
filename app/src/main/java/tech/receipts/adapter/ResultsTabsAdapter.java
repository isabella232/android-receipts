package tech.receipts.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindString;
import butterknife.ButterKnife;
import tech.receipts.R;
import tech.receipts.ui.activity.ResultsActivity;
import tech.receipts.data.model.result.LotteryResultsCollectionResponse;
import tech.receipts.data.model.result.ResultResponse;
import tech.receipts.data.model.result.StatusEnum;
import tech.receipts.ui.fragment.ActiveResultsFragment;
import tech.receipts.ui.fragment.FinishResultsFragment;
import tech.receipts.ui.fragment.ResultsFragmentAbstract;
import timber.log.Timber;

public class ResultsTabsAdapter extends FragmentPagerAdapter {

    private static final int UPCOMING_RESULTS_POS = 0;
    private static final int FINISHED_RESULTS_POS = 1;

    @BindString(R.string.results_tab_upcoming)
    protected String resultsTabUpcoming;

    @BindString(R.string.results_tab_finished)
    protected String resultsTabFinished;

    private LotteryResultsCollectionResponse resultsCollectionResponse;
    private List<ResultsFragmentAbstract> fragments = new ArrayList<>(2);

    private ResultsActivity activity;

    public ResultsTabsAdapter(ResultsActivity activity, LotteryResultsCollectionResponse collectionResponse) {
        super(activity.getSupportFragmentManager());
        this.activity = activity;
        Timber.d("ResultsTabsAdapter: %d", collectionResponse.getTotal());
        this.resultsCollectionResponse = collectionResponse;
        ButterKnife.bind(this, activity);

        updateData();
    }

    public void updateAdapter(ResultsActivity activity, LotteryResultsCollectionResponse resultsCollectionResponse) {
        Timber.d("updateAdapter with notifyDataSetChanged: %d", resultsCollectionResponse.getTotal());
        this.resultsCollectionResponse = resultsCollectionResponse;
        this.activity = activity;

        if (activity != null) {
            try {
                updateData();
                notifyDataSetChanged();
            } catch (IllegalStateException e) {
                Timber.e(e.getMessage());
            }
        }
    }

    private void updateData() {
        LotteryResultsCollectionResponse activeResults = getTabResults(StatusEnum.ACTIVE);
        LotteryResultsCollectionResponse finishedResults = getTabResults(StatusEnum.FINISHED);

        if (fragments.isEmpty()) {
            fragments.add(UPCOMING_RESULTS_POS, ActiveResultsFragment.newInstance(activeResults));
            fragments.add(FINISHED_RESULTS_POS, FinishResultsFragment.newInstance(finishedResults));
        } else {
            fragments.get(UPCOMING_RESULTS_POS).setCollectionResponse(activeResults);
            fragments.get(FINISHED_RESULTS_POS).setCollectionResponse(finishedResults);
        }
    }

    @Override
    public CharSequence getPageTitle(int i) {
        switch (i) {
            case UPCOMING_RESULTS_POS:
                return resultsTabUpcoming;
            case FINISHED_RESULTS_POS:
                return resultsTabFinished;
            default:
                return null;
        }
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int i) {
        Timber.d("ResultsTabsAdapter getItem: %d", i);
        switch (i) {
            case UPCOMING_RESULTS_POS:
            case FINISHED_RESULTS_POS:
                return fragments.get(i);
            default:
                return null;
        }
    }

    private LotteryResultsCollectionResponse getTabResults(StatusEnum status) {
        List<ResultResponse> list = new ArrayList<ResultResponse>();
        for (ResultResponse response : resultsCollectionResponse.getResults()) {
            if (status.equals(response.getStatus())) {
                list.add(response);
            }
        }

        return new LotteryResultsCollectionResponse(list.size(), list);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
