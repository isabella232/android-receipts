package tech.receipts.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import tech.receipts.adapter.ActiveResultsAdapter;
import tech.receipts.data.model.result.LotteryResultsCollectionResponse;
import timber.log.Timber;

public class ActiveResultsFragment extends ResultsFragmentAbstract {

    @BindView(R.id.results_view_animator)
    protected ViewAnimator animator;

    @BindView(R.id.results_recycler)
    protected RecyclerView recycler;

    private Unbinder unbinder;

    public static ActiveResultsFragment newInstance(LotteryResultsCollectionResponse collectionResponse) {
        ActiveResultsFragment fragment = new ActiveResultsFragment();
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

        setupRecyclerView();
        return rootView;
    }

    private void setupRecyclerView() {
        animator.setDisplayedChild(collectionResponse.getTotal() > 0 ? ANIMATOR_LIST : ANIMATOR_EMPTY_LABEL);

        FragmentActivity activity = getActivity();

        if (activity != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            recycler.setLayoutManager(layoutManager);

            ActiveResultsAdapter activeResultsAdapter = new ActiveResultsAdapter(collectionResponse);
            recycler.setAdapter(activeResultsAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
