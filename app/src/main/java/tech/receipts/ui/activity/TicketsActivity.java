package tech.receipts.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tech.receipts.R;
import tech.receipts.adapter.TicketsMonthlyTabsAdapter;
import tech.receipts.components.UserSettingsComponent;
import tech.receipts.data.api.caller.TicketsCaller;
import tech.receipts.data.model.ApiError;
import tech.receipts.data.model.ticket.GetTicketsRequest;
import tech.receipts.data.model.ticket.LotteryTicketsCollectionResponse;
import tech.receipts.ui.fragment.LoginDialogFragment;
import tech.receipts.ui.view.TicketsMvpView;
import timber.log.Timber;

import static xdroid.toaster.Toaster.toastLong;

public class TicketsActivity extends BaseActivity implements TicketsMvpView, ActivityWithOnActivityResult {
    public static final int LOGIN_FORM_REQ_CODE = 102;
    private static final String LOGIN_FORM_TAG = "loginFormTag";
    private static final String STATE_TICKETS = "state-tickets";

    private ProgressDialog progressDialog;

    @BindString(R.string.title_section_tickets_list)
    protected String titleSectionTicketsList;

    @BindView(R.id.viewpager)
    protected ViewPager viewPager;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.tabs)
    protected PagerSlidingTabStrip tabLayout;

    //toast
    @BindString(R.string.toast_no_connection)
    protected String toastNoConnection;

    @BindString(R.string.toast_connection_error)
    protected String toastConnectionError;

    @BindString(R.string.toast_failed_get_tickets)
    protected String toastFailedGetTickets;

    @BindString(R.string.toast_invalid_user_credentials)
    protected String toastInvalidUserCredentials;

    @Inject
    protected UserSettingsComponent userSettingsComponent;

    @Inject
    protected TicketsCaller ticketsPresenter;

    @Inject
    protected LoginDialogFragment loginDialogFragment;

    private LotteryTicketsCollectionResponse collectionResponse = new LotteryTicketsCollectionResponse();

    private TicketsMonthlyTabsAdapter adapter;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        unbinder = ButterKnife.bind(this);
        component().inject(this);

        Timber.d("onCreate%s", savedInstanceState);

        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(true);
            bar.setTitle(titleSectionTicketsList);
        }

        if (savedInstanceState != null) {
            LotteryTicketsCollectionResponse savedInstanceStateSerializable = (LotteryTicketsCollectionResponse) savedInstanceState.getSerializable(STATE_TICKETS);
            if (savedInstanceStateSerializable != null) {
                collectionResponse = savedInstanceStateSerializable;
                Timber.d("onCreate restored savedInstanceState: %d", collectionResponse.getTotal());
            } else {
                Timber.d("onCreate nothing to restore from bundle");
            }
        }

        ticketsPresenter.attachView(this);
        adapter = new TicketsMonthlyTabsAdapter(this, collectionResponse);
        setupViewPager();

        if (collectionResponse.getTotal() > 0) {
            adapter.updateAdapter(this, collectionResponse);
        } else if (isUserLogin()) {
            loadTicketsAndUpdateView(userSettingsComponent.getUserLogin(), userSettingsComponent.getUserPassword(), false);
        } else {
            setupLoginForm();
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Timber.d("onSaveInstanceState: %s", savedInstanceState);

        if (savedInstanceState != null) {
            Timber.d("onSaveInstanceState save: %d", collectionResponse.getTotal());
            savedInstanceState.putSerializable(STATE_TICKETS, collectionResponse);
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOGIN_FORM_REQ_CODE && resultCode == Activity.RESULT_OK) {
            String login = data.getStringExtra(LoginDialogFragment.EXTRA_KEY_LOGIN_STRING);
            String password = data.getStringExtra(LoginDialogFragment.EXTRA_KEY_PASSWORD_STRING);
            boolean store = data.getBooleanExtra(LoginDialogFragment.EXTRA_KEY_STORE_BOOL, false);

            loadTicketsAndUpdateView(login, password, store);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isUserLogin() {
        return userSettingsComponent.getUserLogin() != null && userSettingsComponent.getUserPassword() != null;
    }

    private void loadTicketsAndUpdateView(final String email, final String password, final boolean store) {
        if (collectionResponse.getTotal() == 0) {
            final GetTicketsRequest request = new GetTicketsRequest();
            request.setPassword(password);
            request.setEmail(email);
            request.setStore(store);

            ticketsPresenter.loadTickets(request);

        } else {
            Timber.d("loadResultsAndUpdateView else activity: %s", collectionResponse);
            adapter.updateAdapter(this, collectionResponse);
        }
    }

    private void onExceptionUi(String message) {
        toastLong(message);
    }

    private void setInteractionLocked(boolean lock) {
        if (lock) {
            progressDialog = ProgressDialog.show(this, null,
                    getString(R.string.progress_get_tickets), true, false);
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                try {
                    progressDialog.dismiss();
                } catch (IllegalArgumentException e) {
                    Timber.e(e.getMessage());
                }
                progressDialog = null;
            }
        }
    }

    private void setupLoginForm() {
        try {
            FragmentManager fm = getSupportFragmentManager();

            LoginDialogFragment fragment = (LoginDialogFragment) fm.findFragmentByTag(LOGIN_FORM_TAG);

            if (fragment == null) {
                fragment = loginDialogFragment;
            }

            if (!fragment.isAdded()) {
                loginDialogFragment.setTargetFragment(null, LOGIN_FORM_REQ_CODE);
                loginDialogFragment.show(fm, LOGIN_FORM_TAG);
            }
        } catch (IllegalStateException e) {
            Timber.e(e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager() {
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(adapter.getSelectedTab());
        tabLayout.setViewPager(viewPager);
        Timber.d("setCurrentItem: %d", adapter.getSelectedTab());
    }

    @Override
    public void onErrorResponse(String message) {
        Context applicationContext = getApplicationContext();

        if (applicationContext != null) {

            if (TextUtils.isEmpty(message)) {
                message = toastFailedGetTickets;
            }
            onExceptionUi(message);
        }
    }

    @Override
    public void onNoConnectionError() {
        onErrorResponse(toastNoConnection);
    }

    @Override
    public void onRetryError() {
        onErrorResponse(toastConnectionError);
    }

    @Override
    public void unableToGetTokenError() {
        onErrorResponse(toastFailedGetTickets);
    }

    @Override
    public void startedLoadingData() {
        setInteractionLocked(true);
    }

    @Override
    public void finishedLoadingData() {
        setInteractionLocked(false);
    }

    @Override
    public void onGetTicketsFinished(GetTicketsRequest request, LotteryTicketsCollectionResponse lotteryTicketsCollectionResponse) {
        Context applicationContext = getApplicationContext();

        if (applicationContext != null) {
            if (request.isStore()) {
                userSettingsComponent.setUserLogin(request.getEmail());
                userSettingsComponent.setUserPassword(request.getPassword());
            }

            if (lotteryTicketsCollectionResponse != null) {
                collectionResponse = lotteryTicketsCollectionResponse;
                adapter.updateAdapter(this, lotteryTicketsCollectionResponse);
            }
        }
    }

    @Override
    public void onInvalidUserAuth(ApiError error) {
        Context applicationContext = getApplicationContext();

        if (applicationContext != null) {
            //failed to login, show login form again
            setupLoginForm();
            onExceptionUi(toastInvalidUserCredentials);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

        ticketsPresenter.detachView();
    }
}