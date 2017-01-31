package tech.receipts.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.receipts.R;
import tech.receipts.components.DateFormatter;
import tech.receipts.data.model.result.LotteryResultsCollectionResponse;
import tech.receipts.data.model.result.ResultPrizeResponse;
import tech.receipts.data.model.result.ResultResponse;

public class ActiveResultsAdapter extends RecyclerView.Adapter<ActiveResultsAdapter.ViewHolder> {

    private LotteryResultsCollectionResponse collectionResponse;
    private int total = 0;

    public ActiveResultsAdapter(LotteryResultsCollectionResponse collectionResponse) {
        this.collectionResponse = collectionResponse;
        this.total = collectionResponse.getTotal();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.result_active_row,
                viewGroup, false);

        return new ViewHolder(row);
    }

    @Override
    public int getItemCount() {
        return total;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final ResultResponse response = collectionResponse.getResults().get(position);

        viewHolder.name.setText(response.getName());
        String prizes = getPrizes(response.getPrizes());
        viewHolder.prizes.setText(prizes);
        String date = DateFormatter.formatDateTimeFromApi(response.getDate());
        viewHolder.date.setText(date);
    }

    private String getPrizes(List<ResultPrizeResponse> prizes) {
        String[] tokens = new String[prizes.size()];
        int i = 0;
        for (ResultPrizeResponse prize : prizes) {
            tokens[i] = prize.getName();
            i++;
        }

        return TextUtils.join(", ", tokens);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.name)
        protected TextView name;

        @BindView(R.id.prizes)
        protected TextView prizes;

        @BindView(R.id.date)
        protected TextView date;

        public ViewHolder(View resultRow) {
            super(resultRow);
            ButterKnife.bind(this, resultRow);
        }
    }

}
