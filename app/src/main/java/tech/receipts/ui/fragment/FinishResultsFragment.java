package tech.receipts.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewAnimator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tech.receipts.R;
import tech.receipts.adapter.FinishedResultsAdapter;
import tech.receipts.data.model.result.LotteryResultsCollectionResponse;
import tech.receipts.ui.activity.ResultsActivity;
import timber.log.Timber;

public class FinishResultsFragment extends ResultsFragmentAbstract {
    @BindView(R.id.results_view_animator)
    protected ViewAnimator animator;

    @BindView(R.id.results_recycler)
    protected RecyclerView recycler;

    private Unbinder unbinder;

    public static FinishResultsFragment newInstance(LotteryResultsCollectionResponse collectionResponse) {
        FinishResultsFragment fragment = new FinishResultsFragment();
        fragment.setCollectionResponse(collectionResponse);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            LotteryResultsCollectionResponse savedInstanceStateSerializable = (LotteryResultsCollectionResponse) savedInstanceState.getSerializable(ARG_COLLECTION);
            if (savedInstanceStateSerializable != null) {
                collectionResponse = savedInstanceStateSerializable;
                Timber.d("onCreate restored savedInstanceState: %d", collectionResponse.getTotal());
            } else {
                Timber.d("onCreate nothing to restore from bundle");
            }
        }

        setupRecyclerView(inflater);
        return rootView;
    }

    private void setupRecyclerView(LayoutInflater inflater) {
        animator.setDisplayedChild(collectionResponse.getTotal() > 0 ? ANIMATOR_LIST : ANIMATOR_EMPTY_LABEL);
        ResultsActivity activity = (ResultsActivity) getActivity();

        if (activity != null) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            recycler.setLayoutManager(layoutManager);

            FinishedResultsAdapter adapter = new FinishedResultsAdapter(activity, inflater, collectionResponse);
            recycler.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
