package tech.receipts.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.receipts.components.DateFormatter;
import tech.receipts.R;
import tech.receipts.ui.activity.ResultsActivity;
import tech.receipts.data.model.result.LotteryResultsCollectionResponse;
import tech.receipts.data.model.result.ResultPrizeResponse;
import tech.receipts.data.model.result.ResultPrizeWinnerResponse;
import tech.receipts.data.model.result.ResultResponse;
import timber.log.Timber;

public class FinishedResultsAdapter extends HeaderFooterRecyclerViewAdapter<FinishedResultsAdapter.ViewHolder, FinishedResultsAdapter.HeaderViewHolder, FinishedResultsAdapter.FooterViewHolder> {

    private LayoutInflater vi;
    private LotteryResultsCollectionResponse collectionResponse;
    private ResultsActivity activity;

    public FinishedResultsAdapter(ResultsActivity activity, LayoutInflater vi, LotteryResultsCollectionResponse collectionResponse) {
        Timber.d("activity: %s", activity);
        this.activity = activity;
        this.vi = vi;
        this.collectionResponse = collectionResponse;
    }

    @Override
    protected int getHeaderItemCount() {
        return 0;
    }

    @Override
    protected int getFooterItemCount() {
        return 1;
    }

    @Override
    protected int getContentItemCount() {
        return collectionResponse.getTotal();
    }

    @Override
    protected HeaderViewHolder onCreateHeaderItemViewHolder(ViewGroup viewGroup, int headerViewType) {
        return null;
    }

    @Override
    protected FooterViewHolder onCreateFooterItemViewHolder(ViewGroup viewGroup, int footerViewType) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_results_finished_footer,
                viewGroup, false);

        return new FooterViewHolder(row, activity);
    }

    @Override
    protected ViewHolder onCreateContentItemViewHolder(ViewGroup viewGroup, int contentViewType) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_finished_row,
                viewGroup, false);

        return new ViewHolder(row);
    }

    @Override
    protected void onBindHeaderItemViewHolder(HeaderViewHolder headerViewHolder, int position) {

    }

    @Override
    protected void onBindFooterItemViewHolder(FooterViewHolder footerViewHolder, int position) {

    }

    @Override
    protected void onBindContentItemViewHolder(ViewHolder viewHolder, int position) {
        final ResultResponse response = collectionResponse.getResults().get(position);

        viewHolder.name.setText(response.getName());
        String date = DateFormatter.formatDateFromApi(response.getDate());
        viewHolder.date.setText(date);
        viewHolder.winners.removeAllViews();
        bindPrizeWinners(vi, viewHolder.winners, response.getPrizes());
    }

    private void bindPrizeWinners(LayoutInflater vi, ViewGroup parent, List<ResultPrizeResponse> prizes) {
        int count = 0;
        for (int i = 0; i < prizes.size(); ++i) {
            ResultPrizeResponse prize = prizes.get(i);
            for (int j = 0; j < prize.getWinners().size(); ++j) {
                ResultPrizeWinnerResponse winner = prize.getWinners().get(j);
                @SuppressLint("InflateParams")
                View winnerView = vi.inflate(R.layout.result_winner_row, null);

                ((TextView) winnerView.findViewById(R.id.prize)).setText(prize.getName());
                ((TextView) winnerView.findViewById(R.id.code)).setText(winner.getCode());
                parent.addView(winnerView, count++);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        protected TextView name;

        @BindView(R.id.winners)
        protected ViewGroup winners;

        @BindView(R.id.date)
        protected TextView date;

        public ViewHolder(View resultRow) {
            super(resultRow);
            ButterKnife.bind(this, resultRow);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View resultRow, ResultsActivity activity) {
            super(resultRow);

            setupCheckWinButton(resultRow, activity);
        }

        private void setupCheckWinButton(View resultRow, final ResultsActivity activity) {
            Button button = (Button) resultRow.findViewById(R.id.button_check_if_win);
            button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    activity.onCheckWinButtonClick(v);
                }

            });
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View resultRow) {
            super(resultRow);
        }
    }

}
