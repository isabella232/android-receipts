package tech.receipts.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindString;
import butterknife.ButterKnife;
import tech.receipts.R;
import tech.receipts.components.DateFormatter;
import tech.receipts.data.model.ticket.LotteryTicketsCollectionResponse;
import tech.receipts.data.model.ticket.TicketResponse;
import tech.receipts.ui.fragment.TicketsCardFragment;
import timber.log.Timber;

public class TicketsMonthlyTabsAdapter extends FragmentPagerAdapter {

    public static final String TAB_NAME_FORMAT = "%s (%d)";
    @BindString(R.string.tickets_card_11_2015)
    protected String ticketsCard112015;

    @BindString(R.string.tickets_card_12_2015)
    protected String ticketsCard122015;

    @BindString(R.string.tickets_card_01_2016)
    protected String ticketsCard012016;

    @BindString(R.string.tickets_card_02_2016)
    protected String ticketsCard022016;

    @BindString(R.string.tickets_card_03_2016)
    protected String ticketsCard032016;

    @BindString(R.string.tickets_card_04_2016)
    protected String ticketsCard042016;

    @BindString(R.string.tickets_card_05_2016)
    protected String ticketsCard052016;

    @BindString(R.string.tickets_card_06_2016)
    protected String ticketsCard062016;

    @BindString(R.string.tickets_card_07_2016)
    protected String ticketsCard072016;

    @BindString(R.string.tickets_card_08_2016)
    protected String ticketsCard082016;

    @BindString(R.string.tickets_card_09_2016)
    protected String ticketsCard092016;

    @BindString(R.string.tickets_card_10_2016)
    protected String ticketsCard102016;

    @BindString(R.string.tickets_card_11_2016)
    protected String ticketsCard112016;

    @BindString(R.string.tickets_card_12_2016)
    protected String ticketsCard122016;

    @BindString(R.string.tickets_card_01_2017)
    protected String ticketsCard012017;

    @BindString(R.string.tickets_card_02_2017)
    protected String ticketsCard022017;

    @BindString(R.string.tickets_card_03_2017)
    protected String ticketsCard032017;

    @BindString(R.string.tickets_card_04_2017)
    protected String ticketsCard042017;

    @BindString(R.string.tickets_specials)
    protected String ticketsSpecials;

    private Map<LocalDateTime, String> months = new LinkedHashMap<>();
    private List<LotteryTicketsCollectionResponse> collections = new ArrayList<>();
    private List<String> monthNames = new ArrayList<>();
    private List<TicketsCardFragment> fragments = new ArrayList<>();
    private LotteryTicketsCollectionResponse collectionResponse;

    private FragmentActivity activity;

    private int selectedTab = -1;

    public int getSelectedTab() {
        return selectedTab;
    }

    public TicketsMonthlyTabsAdapter(FragmentActivity activity, LotteryTicketsCollectionResponse collectionResponse) {
        super(activity.getSupportFragmentManager());
        ButterKnife.bind(this, activity);
        updateAdapter(activity, collectionResponse);
        updateMonths();
        prepareTabsData();
    }

    public void updateAdapter(FragmentActivity activity, LotteryTicketsCollectionResponse collectionResponse) {
        Timber.d("updateAdapter with notify: %d", collectionResponse.getTotal());
        this.collectionResponse = collectionResponse;
        this.activity = activity;

        if (activity != null) {
            try {
                prepareTabsData();
                notifyDataSetChanged();
            } catch (IllegalStateException e) {
                Timber.e(e.getMessage());
            }
        } else {
            Timber.d("updateAdapter: skip update because null activity");
        }
    }

    private void updateMonths() {
        months.put(LocalDateTime.parse("01.11.2015", DateFormatter.DATE_FORMATTER), ticketsCard112015);
        months.put(LocalDateTime.parse("01.12.2015", DateFormatter.DATE_FORMATTER), ticketsCard122015);
        months.put(LocalDateTime.parse("01.01.2016", DateFormatter.DATE_FORMATTER), ticketsCard012016);
        months.put(LocalDateTime.parse("01.02.2016", DateFormatter.DATE_FORMATTER), ticketsCard022016);
        months.put(LocalDateTime.parse("01.03.2016", DateFormatter.DATE_FORMATTER), ticketsCard032016);
        months.put(LocalDateTime.parse("01.04.2016", DateFormatter.DATE_FORMATTER), ticketsCard042016);
        months.put(LocalDateTime.parse("01.05.2016", DateFormatter.DATE_FORMATTER), ticketsCard052016);
        months.put(LocalDateTime.parse("01.06.2016", DateFormatter.DATE_FORMATTER), ticketsCard062016);
        months.put(LocalDateTime.parse("01.07.2016", DateFormatter.DATE_FORMATTER), ticketsCard072016);
        months.put(LocalDateTime.parse("01.08.2016", DateFormatter.DATE_FORMATTER), ticketsCard082016);
        months.put(LocalDateTime.parse("01.09.2016", DateFormatter.DATE_FORMATTER), ticketsCard092016);
        months.put(LocalDateTime.parse("01.10.2016", DateFormatter.DATE_FORMATTER), ticketsCard102016);
        months.put(LocalDateTime.parse("01.11.2016", DateFormatter.DATE_FORMATTER), ticketsCard112016);
        months.put(LocalDateTime.parse("01.12.2016", DateFormatter.DATE_FORMATTER), ticketsCard122016);
        months.put(LocalDateTime.parse("01.01.2017", DateFormatter.DATE_FORMATTER), ticketsCard012017);
        months.put(LocalDateTime.parse("01.02.2017", DateFormatter.DATE_FORMATTER), ticketsCard022017);
        months.put(LocalDateTime.parse("01.03.2017", DateFormatter.DATE_FORMATTER), ticketsCard032017);
        months.put(LocalDateTime.parse("01.04.2017", DateFormatter.DATE_FORMATTER), ticketsCard042017);

        prepareTabsData();
    }

    private void prepareTabsData() {
        Timber.d("prepareTabsData: %d", collectionResponse.getTotal());
        collections = new ArrayList<>();
        monthNames = new ArrayList<>();

        Boolean updateFragments = true;

        if (fragments.isEmpty()) {
            updateFragments = false;
        }

        Timber.d("prepareTabsData: updateFragments set to %s", updateFragments.toString());

        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int monthOfYear = now.getMonthOfYear();

        int pos = 0;

        for (Map.Entry<LocalDateTime, String> entry : months.entrySet()) {
            LocalDateTime keyDate = entry.getKey();

            if (keyDate.getYear() == year && keyDate.getMonthOfYear() == monthOfYear) {
                selectedTab = pos;
            }

            if (keyDate.getYear() < year || (keyDate.getYear() == year && keyDate.getMonthOfYear() <= monthOfYear)) {
                LotteryTicketsCollectionResponse collection = getCollectionForTab(collectionResponse, pos, keyDate.getYear(), keyDate.getMonthOfYear());

                if (collection.getTotal() > 0) {
                    collections.add(pos, collection);
                    monthNames.add(pos, String.format(Locale.getDefault(), TAB_NAME_FORMAT, entry.getValue(), collection.getTotal()));

                    if (updateFragments) {
                        fragments.get(pos).update(collection);
                    } else {
                        fragments.add(pos, TicketsCardFragment.newInstance(collection));
                    }
                    ++pos;
                }
            }
        }

        if (selectedTab == -1) {
            selectedTab = collections.size() - 1;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return monthNames.get(position);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int i) {
        Timber.d("getItem collection: %d", collections.get(i).getTotal());
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return collections.size();
    }

    private LotteryTicketsCollectionResponse getCollectionForTab(LotteryTicketsCollectionResponse collectionResponse, int tab, int year, int month) {
        Timber.d("getCollectionForTab: tab: %d", tab);

        List<TicketResponse> list = new ArrayList<>();

        for (TicketResponse response : collectionResponse.getTickets()) {
            LocalDateTime localDateTime = DateTime.parse(response.getDate()).toLocalDateTime();

            if (localDateTime.getYear() == year && localDateTime.getMonthOfYear() == month) {
                list.add(response);
            }
        }

        return new LotteryTicketsCollectionResponse(list.size(), list);
    }
}
