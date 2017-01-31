package tech.receipts.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tech.receipts.R;
import tech.receipts.components.DateFormatter;
import tech.receipts.components.PriceFormatter;
import tech.receipts.data.model.ticket.LotteryTicketsCollectionResponse;
import tech.receipts.data.model.ticket.TicketResponse;
import timber.log.Timber;

public class TicketsHistoryAdapter extends RecyclerView.Adapter<TicketsHistoryAdapter.ViewHolder> {

    private LotteryTicketsCollectionResponse collectionResponse = new LotteryTicketsCollectionResponse();
    private int total = 0;

    public TicketsHistoryAdapter(LotteryTicketsCollectionResponse collectionResponse) {
        this.collectionResponse = collectionResponse;
        this.total = collectionResponse.getTotal();
    }

    public void setCollectionResponse(LotteryTicketsCollectionResponse collectionResponse) {
        Timber.d("update with notify: %d", collectionResponse.getTotal());
        this.collectionResponse = collectionResponse;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View row = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_row,
                viewGroup, false);

        return new ViewHolder(row);
    }

    @Override
    public int getItemCount() {
        return total;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final TicketResponse ticketResponse = collectionResponse.getTickets().get(position);
        viewHolder.code.setText(ticketResponse.getCode());
        viewHolder.amount.setText(PriceFormatter.formatAmount(ticketResponse.getAmount()));

        String date = DateFormatter.formatDateFromApi(ticketResponse.getDate());
        viewHolder.date.setText(date);
        viewHolder.pon.setText(ticketResponse.getPurchaseOrderNumber());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.code)
        protected TextView code;

        @BindView(R.id.amount)
        protected TextView amount;

        @BindView(R.id.date)
        protected TextView date;

        @BindView(R.id.pon)
        protected TextView pon;

        public ViewHolder(View historyRow) {
            super(historyRow);
            ButterKnife.bind(this, historyRow);
        }
    }

}
