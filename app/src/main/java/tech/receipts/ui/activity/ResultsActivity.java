package tech.receipts.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import java.util.Map;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tech.receipts.R;
import tech.receipts.adapter.ResultsTabsAdapter;
import tech.receipts.components.UserSettingsComponent;
import tech.receipts.components.WinnerCodeCheckComponent;
import tech.receipts.data.api.caller.ResultsCaller;
import tech.receipts.data.api.caller.TicketsCaller;
import tech.receipts.data.model.ApiError;
import tech.receipts.data.model.result.LotteryResultsCollectionResponse;
import tech.receipts.data.model.ticket.GetTicketsRequest;
import tech.receipts.data.model.ticket.LotteryTicketsCollectionResponse;
import tech.receipts.ui.fragment.LoginDialogFragment;
import tech.receipts.ui.view.ResultsMvpView;
import tech.receipts.ui.view.TicketsMvpView;
import timber.log.Timber;

import static xdroid.toaster.Toaster.toastLong;


public class ResultsActivity extends BaseActivity implements ResultsMvpView, TicketsMvpView, ActivityWithOnActivityResult {

    public static final int LOGIN_FORM_REQ_CODE = 102;
    private static final String LOGIN_FORM_TAG = "loginFormTag";

    public static final String STATE_RESULTS = "state-results";
    private ProgressDialog progressDialog;

    @BindString(R.string.progress_get_results)
    protected String progressGetResults;

    @BindString(R.string.toast_failed_check_win_results)
    protected String toastFailedCheckWinResults;

    @BindString(R.string.toast_found_winner_code)
    protected String toastFoundWinnerCode;

    @BindString(R.string.toast_found_winner_codes)
    protected String toastFoundWinnerCodes;

    @BindString(R.string.toast_no_winner_code)
    protected String toastNoWinnerCode;

    @BindString(R.string.title_section_results)
    protected String titleSectionResults;

    //toast
    @BindString(R.string.toast_no_connection)
    protected String toastNoConnection;

    @BindString(R.string.toast_connection_error)
    protected String toastConnectionError;

    @BindString(R.string.toast_failed_get_results)
    protected String toastFailedGetResults;

    @BindString(R.string.toast_invalid_user_credentials)
    protected String toastInvalidUserCredentials;

    @BindView(R.id.viewpager)
    protected ViewPager viewPager;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.tabs)
    protected TabLayout tabLayout;

    @Inject
    protected UserSettingsComponent userSettingsComponent;

    @Inject
    protected ResultsCaller resultsPresenter;

    @Inject
    protected TicketsCaller ticketsPresenter;

    @Inject
    protected WinnerCodeCheckComponent winnerCodeCheckComponent;

    @Inject
    protected LoginDialogFragment loginDialogFragment;

    private Unbinder unbinder;

    protected ResultsTabsAdapter adapter;

    private LotteryResultsCollectionResponse collectionResponse = new LotteryResultsCollectionResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);
        setContentView(R.layout.activity_results);
        unbinder = ButterKnife.bind(this);

        Timber.d("onCreate: %s", savedInstanceState);

        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setHomeButtonEnabled(true);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowTitleEnabled(true);
            bar.setTitle(titleSectionResults);
        }

        if (savedInstanceState != null) {
            LotteryResultsCollectionResponse savedInstanceStateSerializable = (LotteryResultsCollectionResponse) savedInstanceState.getSerializable(STATE_RESULTS);
            if (savedInstanceStateSerializable != null) {
                collectionResponse = savedInstanceStateSerializable;
                Timber.d("onCreate restored savedInstanceState: %d", collectionResponse.getTotal());
            } else {
                Timber.d("onCreate nothing to restore from bundle");
            }
        }

        resultsPresenter.attachView(this);
        ticketsPresenter.attachView(this);
        adapter = new ResultsTabsAdapter(this, collectionResponse);

        setupViewPager();
        loadResultsAndUpdateView();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Timber.d("onSaveInstanceState save: %d", collectionResponse.getTotal());
            savedInstanceState.putSerializable(STATE_RESULTS, collectionResponse);
        }

        super.onSaveInstanceState(savedInstanceState);
    }


    private void loadResultsAndUpdateView() {
        if (collectionResponse.getTotal() == 0) {
            resultsPresenter.loadResults();
        } else {
            Timber.d("loadResultsAndUpdateView else activity: %s", ResultsActivity.this);
            adapter.updateAdapter(ResultsActivity.this, collectionResponse);
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
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setInteractionLocked(boolean lock) {
        if (lock) {
            progressDialog = ProgressDialog.show(this, null, progressGetResults, true, false);
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

    private void onExceptionUi(String message) {
        toastLong(message);
    }

    /////
    public void onCheckWinButtonClick(View v) {
        checkUserLoginData();
    }

    private void checkUserLoginData() {
        if (userSettingsComponent.getUserLogin() != null && userSettingsComponent.getUserPassword() != null) {
            loadTicketsAndShowToast(userSettingsComponent.getUserLogin(), userSettingsComponent.getUserPassword(), false);
        } else {
            setupLoginForm();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOGIN_FORM_REQ_CODE && resultCode == Activity.RESULT_OK) {
            String login = data.getStringExtra(LoginDialogFragment.EXTRA_KEY_LOGIN_STRING);
            String password = data.getStringExtra(LoginDialogFragment.EXTRA_KEY_PASSWORD_STRING);
            boolean store = data.getBooleanExtra(LoginDialogFragment.EXTRA_KEY_STORE_BOOL, false);

            loadTicketsAndShowToast(login, password, store);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadTicketsAndShowToast(final String email, final String password, final boolean store) {

        final GetTicketsRequest request = new GetTicketsRequest();
        request.setPassword(password);
        request.setEmail(email);
        request.setStore(store);

        ticketsPresenter.loadTickets(request);
    }

    private void checkWinTickets(
            LotteryTicketsCollectionResponse tickets,
            LotteryResultsCollectionResponse finishedResults
    ) {
        Context applicationContext = getApplicationContext();

        if (applicationContext != null) {

            Map<String, String> userWinnerCodes = winnerCodeCheckComponent.checkWinTickets(tickets, finishedResults);

            if (userWinnerCodes.isEmpty()) {
                Timber.d("Not found winner codes");
                toastLong(toastNoWinnerCode);

            } else {
                Timber.d("User has winner codes: %d", userWinnerCodes.size());

                String message = toastFoundWinnerCode;

                if (userWinnerCodes.size() > 1) {
                    message = toastFoundWinnerCodes;
                }

                String[] strings = new String[userWinnerCodes.size()];
                int i = 0;

                for (Map.Entry<String, String> entry : userWinnerCodes.entrySet()) {
                    String s = entry.getKey() + " : " + entry.getValue();
                    strings[i++] = s;
                }

                message += TextUtils.join(",", strings);

                //TODO change to dialog or highlight win codes
                toastLong(message);
            }
        }
    }

    @Override
    public void onGetResultsFinished(LotteryResultsCollectionResponse resultsCollectionResponse) {
        Context applicationContext = getApplicationContext();

        if (applicationContext != null) {
            if (resultsCollectionResponse != null) {
                collectionResponse = resultsCollectionResponse;
                Timber.d("loadResultsAndUpdateView activity: %s", ResultsActivity.this);
                adapter.updateAdapter(ResultsActivity.this, collectionResponse);
            }
        }
    }

    @Override
    public void onErrorResponse(String message) {
        Context applicationContext = getApplicationContext();

        if (applicationContext != null) {
            if (TextUtils.isEmpty(message)) {
                message = toastFailedGetResults;
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
        onErrorResponse(toastFailedGetResults);
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
                Timber.d("Loaded: %d", lotteryTicketsCollectionResponse.getTotal());
                checkWinTickets(lotteryTicketsCollectionResponse, collectionResponse);
            } else {
                toastLong(toastFailedCheckWinResults);
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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

        resultsPresenter.detachView();
        ticketsPresenter.detachView();
    }
}
