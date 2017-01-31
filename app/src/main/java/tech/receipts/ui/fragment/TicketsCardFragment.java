package tech.receipts.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import tech.receipts.adapter.TicketsHistoryAdapter;
import tech.receipts.data.model.ticket.LotteryTicketsCollectionResponse;
import timber.log.Timber;

public class TicketsCardFragment extends Fragment {

    private static final int ANIMATOR_LIST = 0;
    private static final int ANIMATOR_EMPTY_LABEL = 1;

    private static final String ARG_COLLECTION = "collection";

    @BindView(R.id.results_view_animator)
    protected ViewAnimator animator;

    @BindView(R.id.tickets_recycler)
    protected RecyclerView recycler;

    private LotteryTicketsCollectionResponse collectionResponse = new LotteryTicketsCollectionResponse();

    private TicketsHistoryAdapter ticketsHistoryAdapter;

    private Unbinder unbinder;

    public void update(LotteryTicketsCollectionResponse collectionResponse) {
        Timber.d("update with notify: %d", collectionResponse.getTotal());
        this.collectionResponse = collectionResponse;
    }

    public static TicketsCardFragment newInstance(LotteryTicketsCollectionResponse collectionResponse) {
        Timber.d("newInstance: %d", collectionResponse.getTotal());
        TicketsCardFragment fragment = new TicketsCardFragment();
        fragment.update(collectionResponse);
        return fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("onCreateView: %s", savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_card, container, false);
        unbinder = ButterKnife.bind(this, rootView);
//        ViewCompat.setElevation(rootView, 50);

        if (savedInstanceState != null) {
            LotteryTicketsCollectionResponse savedInstanceStateSerializable = (LotteryTicketsCollectionResponse) savedInstanceState.getSerializable(ARG_COLLECTION);
            if (savedInstanceStateSerializable != null) {
                collectionResponse = savedInstanceStateSerializable;
                Timber.d("onCreate restored savedInstanceState: %d", collectionResponse.getTotal());
            } else {
                Timber.d("onCreate nothing to restore from bundle");
            }
        }

        ticketsHistoryAdapter = new TicketsHistoryAdapter(collectionResponse);

        setupRecyclerView();
        return rootView;
    }

    private void setupRecyclerView() {
        Timber.d("setupRecyclerView: %d", collectionResponse.getTotal());
        FragmentActivity activity = getActivity();

        if (activity != null) {
            animator.setDisplayedChild(collectionResponse.getTotal() > 0 ? ANIMATOR_LIST : ANIMATOR_EMPTY_LABEL);

            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            recycler.setLayoutManager(layoutManager);

            ticketsHistoryAdapter.setCollectionResponse(collectionResponse);
            recycler.setAdapter(ticketsHistoryAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}