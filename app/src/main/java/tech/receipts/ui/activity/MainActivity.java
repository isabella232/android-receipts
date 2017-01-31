package tech.receipts.ui.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ViewAnimator;

import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.joda.time.DateTime;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tech.receipts.BuildConfig;
import tech.receipts.R;
import tech.receipts.adapter.TradesAdapter;
import tech.receipts.components.DateFormatter;
import tech.receipts.components.UserSettingsComponent;
import tech.receipts.components.autocomplete.PosAutocompleteAdapter;
import tech.receipts.components.autocomplete.TaxAutocompleteAdapter;
import tech.receipts.components.validation.AddTicketInputValidator;
import tech.receipts.components.validation.Issue;
import tech.receipts.data.DataManager;
import tech.receipts.data.api.caller.CreateTicketCaller;
import tech.receipts.data.api.caller.GetTradesCaller;
import tech.receipts.data.model.ApiError;
import tech.receipts.data.model.autocomplete.Autocomplete;
import tech.receipts.data.model.ticket.AddTicketRequest;
import tech.receipts.data.model.ticket.AgreementsRequest;
import tech.receipts.data.model.ticket.AmountResponse;
import tech.receipts.data.model.ticket.CurrencyEnum;
import tech.receipts.data.model.ticket.TicketResponse;
import tech.receipts.data.model.trade.LotteryTradesCollectionResponse;
import tech.receipts.exception.TicketValidationException;
import tech.receipts.ui.fragment.DatePickerFragment;
import tech.receipts.ui.fragment.RememberUserDialogFragment;
import tech.receipts.ui.view.CreateTicketMvpView;
import tech.receipts.ui.view.TradesMvpView;
import timber.log.Timber;

import static xdroid.toaster.Toaster.toastLong;

public class MainActivity extends BaseActivity implements CreateTicketMvpView, TradesMvpView, ActivityWithOnActivityResult {

    public static final int PICK_DATE_REQ_CODE = 101;
    private static final String DATE_PICKER_TAG = "datePickerTag";

    public static final int USER_DATA_FORM_REQ_CODE = 102;
    private static final String USER_DATA_TAG = "userDataTag";

    private static final int ANIMATOR_BUTTON = 0;
    private static final int ANIMATOR_PROGRESS = 1;

    private final Handler handler = new Handler();

    @Inject
    protected UserSettingsComponent userSettingsComponent;

    @Inject
    protected CreateTicketCaller createTicketPresenter;

    @Inject
    protected GetTradesCaller getTradesPresenter;

    @Inject
    protected TradesAdapter tradesAdapter;

    @Inject
    protected DataManager dataManager;

    @BindString(R.string.title_section_add_ticket)
    protected String titleSectionAddTicket;

    @BindString(R.string.toast_no_connection)
    protected String toastNoConnection;

    @BindString(R.string.toast_connection_error)
    protected String toastConnectionError;

    @BindString(R.string.toast_failed_create_ticket)
    protected String toastFailedCreateTicket;

    @BindString(R.string.toast_failed_get_trades)
    protected String toastFailedGetTrades;

    @BindString(R.string.snackbar_reset_form)
    protected String snackbarResetForm;

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    @BindView(R.id.nav_view)
    protected NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    protected DrawerLayout drawerLayout;

    @BindView(R.id.add_ticket_button)
    protected Button addTicketButton;

    @BindView(R.id.add_ticket_animator)
    protected ViewAnimator addTicketAnimator;

    @BindView(R.id.tax_edit_text)
    protected MaterialAutoCompleteTextView taxRegistrationNumberInputLayout;

    @BindView(R.id.pos_edit_text)
    protected MaterialAutoCompleteTextView pointOfSaleInputLayout;

    @BindView(R.id.pon_edit_text)
    protected MaterialEditText purchaseOrderNumberInputLayout;

    @BindView(R.id.amount_edit_text)
    protected MaterialEditText amountInputLayout;

    @BindView(R.id.trade_edit_text)
    protected MaterialBetterSpinner tradeInputLayout;

    @BindView(R.id.purchase_date_edit_text)
    protected MaterialEditText purchaseDateInputLayout;

    @BindView(R.id.user_phone_edit_text)
    protected MaterialEditText userPhoneInputLayout;

    @BindView(R.id.user_email_edit_text)
    protected MaterialEditText userEmailInputLayout;

    @BindView(R.id.terms_of_service_checkbox)
    protected CheckBox termsOfServiceLayout;

    @BindView(R.id.personal_data_processing_checkbox)
    protected CheckBox personalDataProcessingLayout;

    @BindView(R.id.use_my_effigy_checkbox)
    protected CheckBox useMyEffigyLayout;

    @BindView(R.id.pick_date_button)
    protected ImageButton pickDateButton;

    private ArrayAdapter<String> taxAdapter;
    private ArrayAdapter<String> posAdapter;

    @Inject
    protected PosAutocompleteAdapter posAutocompleteAdapter;

    @Inject
    protected TaxAutocompleteAdapter taxAutocompleteAdapter;

    @Inject
    protected DatePickerFragment datePickerFragment;

    @Inject
    protected RememberUserDialogFragment rememberUserDialogFragment;

    private Unbinder unbinder;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        saveControlState(savedInstanceState, "state_tax", taxRegistrationNumberInputLayout.getText());
        saveControlState(savedInstanceState, "state_pos", pointOfSaleInputLayout.getText());
        saveControlState(savedInstanceState, "state_pon", purchaseOrderNumberInputLayout.getText());
        saveControlState(savedInstanceState, "state_amount", amountInputLayout.getText());
        saveControlState(savedInstanceState, "state_trade", tradeInputLayout.getText());
        saveControlState(savedInstanceState, "state_date", purchaseDateInputLayout.getText());
        saveControlState(savedInstanceState, "state_phone", userPhoneInputLayout.getText());
        saveControlState(savedInstanceState, "state_email", userEmailInputLayout.getText());

        savedInstanceState.putBoolean("state_terms", termsOfServiceLayout.isChecked());
        savedInstanceState.putBoolean("state_personal_data", personalDataProcessingLayout.isChecked());
        savedInstanceState.putBoolean("state_use_my_effigy", useMyEffigyLayout.isChecked());

        super.onSaveInstanceState(savedInstanceState);
    }

    private void saveControlState(Bundle savedInstanceState, String stateName, Editable editable) {
        savedInstanceState.putString(stateName, editable.toString());
    }

    private void restoreControlState(Bundle savedInstanceState, String stateName, CheckBox editable) {
        boolean value = savedInstanceState.getBoolean(stateName);
        editable.setChecked(value);
    }

    private void restoreControlState(Bundle savedInstanceState, String stateName, MaterialAutoCompleteTextView editable) {
        String value = savedInstanceState.getString(stateName);

        if (!TextUtils.isEmpty(value)) {
            editable.setText(value);
        }
    }

    private void restoreControlState(Bundle savedInstanceState, String stateName, MaterialEditText editable) {
        String value = savedInstanceState.getString(stateName);

        if (!TextUtils.isEmpty(value)) {
            editable.setText(value);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component().inject(this);
        userSettingsComponent.handleDayNightMode(getDelegate());

        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        dataManager.getPosCollection()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Autocomplete>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Failed to load pos collection from database");
                    }

                    @Override
                    public void onNext(List<Autocomplete> list) {
                        Timber.d("Loaded poses: %s", list);
                        posAutocompleteAdapter.update(list);
                    }
                });

        dataManager.getTaxCollection()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Failed to load tax collection from database");
                    }

                    @Override
                    public void onNext(List<String> list) {
                        Timber.d("Loaded tax: %s", list);
                        taxAutocompleteAdapter.update(list);
                        taxAdapter = taxAutocompleteAdapter.getAdapter();
                        taxRegistrationNumberInputLayout.setAdapter(taxAdapter);
                    }
                });


        bindViews();
        resetValues();

        if (savedInstanceState != null) {
            restoreControlState(savedInstanceState, "state_tax", taxRegistrationNumberInputLayout);
            restoreControlState(savedInstanceState, "state_pos", pointOfSaleInputLayout);
            restoreControlState(savedInstanceState, "state_pon", purchaseOrderNumberInputLayout);
            restoreControlState(savedInstanceState, "state_amount", amountInputLayout);
//            restoreControlState(savedInstanceState, "state_trade", tradeInputLayout);
            restoreControlState(savedInstanceState, "state_date", purchaseDateInputLayout);
            restoreControlState(savedInstanceState, "state_phone", userPhoneInputLayout);
            restoreControlState(savedInstanceState, "state_email", userEmailInputLayout);
            restoreControlState(savedInstanceState, "state_terms", termsOfServiceLayout);
            restoreControlState(savedInstanceState, "state_personal_data", personalDataProcessingLayout);
            restoreControlState(savedInstanceState, "state_use_my_effigy", useMyEffigyLayout);
        }

        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle(titleSectionAddTicket);
        }

        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        setupPickDateButton();
        setupSearchButton();
    }

    public void onNavigationDrawerItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_add_ticket: {
                resetValues();
                break;
            }
            case R.id.nav_tickets_history: {
                Intent i = new Intent(this, TicketsActivity.class);
                startActivity(i);
                break;
            }
            case R.id.nav_results: {
                Intent i = new Intent(this, ResultsActivity.class);
                startActivity(i);
                break;
            }
            case R.id.nav_options: {
                Intent i = new Intent(this, OptionsActivity.class);
                startActivity(i);
                break;
            }
            case R.id.nav_rate:
                showMarketAppIn();
                break;

            case R.id.nav_about:
                Intent i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;

            default:

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        disableNavigationViewScrollbars(navigationView);
        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        onNavigationDrawerItemSelected(menuItem);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    private void showMarketAppIn() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="
                    + BuildConfig.APPLICATION_ID)));
        }
    }

    private void bindViews() {
        createTicketPresenter.attachView(this);
        getTradesPresenter.attachView(this);

        pointOfSaleInputLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    String tax = taxRegistrationNumberInputLayout.getText().toString();
                    posAdapter = posAutocompleteAdapter.getAdapter(tax);
                    pointOfSaleInputLayout.setAdapter(posAdapter);
                    if (posAdapter.getCount() == 1) {
                        pointOfSaleInputLayout.setText(posAdapter.getItem(0));
                    }
                }
            }
        });

        if (tradesAdapter.isDefault()) {
            getTradesPresenter.loadTrades();
        }

        tradeInputLayout.setAdapter(tradesAdapter.getAdapter());
    }

    private void resetValues() {
        taxRegistrationNumberInputLayout.setText("");
        pointOfSaleInputLayout.setText("");
        purchaseOrderNumberInputLayout.setText("");
        amountInputLayout.setText("");
//        tradeInputLayout.setText("");

        String date = "";
        if (userSettingsComponent.getDefaultCurrentDate()) {
            DateTime dateTime = new DateTime();
            date = DateFormatter.DATE_FORMATTER.print(dateTime);
        }

        purchaseDateInputLayout.setText(date);
        userEmailInputLayout.setText("");
        termsOfServiceLayout.setChecked(false);
        personalDataProcessingLayout.setChecked(false);
        useMyEffigyLayout.setChecked(false);

        String userPhone = userSettingsComponent.getUserPhone();
        if (TextUtils.isEmpty(userPhone)) {
            userPhoneInputLayout.setText(R.string.poland_phone_prefix);
        } else {
            userPhoneInputLayout.setText(userPhone);
        }

        String userEmail = userSettingsComponent.getUserEmail();
        if (!TextUtils.isEmpty(userEmail)) {
            userEmailInputLayout.setText(userEmail);
        }
    }

    private void setupRememberForm() {
        try {
            FragmentManager fm = getSupportFragmentManager();

            RememberUserDialogFragment fragment = (RememberUserDialogFragment) fm.findFragmentByTag(DATE_PICKER_TAG);

            if (fragment == null) {
                fragment = rememberUserDialogFragment;
            }

            if (!fragment.isAdded()) {
                fragment.setTargetFragment(null, USER_DATA_FORM_REQ_CODE);
                fragment.show(fm, USER_DATA_TAG);
            }
        } catch (IllegalStateException e) {
            Timber.e(e.getMessage());
        }
    }

    private void setupDateDialog() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            DatePickerFragment fragment = (DatePickerFragment) fm.findFragmentByTag(DATE_PICKER_TAG);

            if (fragment == null) {
                fragment = datePickerFragment;
            }

            if (!fragment.isAdded()) {
                fragment.setTargetFragment(null, PICK_DATE_REQ_CODE);
                fragment.show(fm, DATE_PICKER_TAG);
            }

        } catch (IllegalStateException e) {
            Timber.e(e.getMessage());
        }
    }

    private void setupPickDateButton() {
        pickDateButton.setBackgroundResource(R.drawable.ic_date_range_black_48dp);
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseDateInputLayout.setError(null);
                setupDateDialog();
            }
        });

        purchaseDateInputLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    purchaseDateInputLayout.setError(null);
                    setupDateDialog();
                }
            }
        });
    }

    private void setupSearchButton() {
        addTicketButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clearErrors();
                validateAndPerformRequest();
            }

        });
    }

    private void clearErrors() {
        taxRegistrationNumberInputLayout.setError(null);
        pointOfSaleInputLayout.setError(null);
        purchaseOrderNumberInputLayout.setError(null);
        amountInputLayout.setError(null);
        tradeInputLayout.setError(null);
        purchaseDateInputLayout.setError(null);
        userPhoneInputLayout.setError(null);
        userEmailInputLayout.setError(null);
        termsOfServiceLayout.setError(null);
        personalDataProcessingLayout.setError(null);
        useMyEffigyLayout.setError(null);
    }

    private void validateAndPerformRequest() {
        try {
            performAddTicket(getValidatedInput());
        } catch (TicketValidationException e) {
            handleValidationIssues(e.getIssues());
        }
    }

    private void handleValidationIssues(Collection<Issue> issues) {
        for (Issue issue : issues) {
            switch (issue.getField()) {
                case TAX_REGISTRATION_NUMBER:
                    taxRegistrationNumberInputLayout.setError(issue.getDetailMessage());
                    break;
                case POINT_OF_SALE:
                    pointOfSaleInputLayout.setError(issue.getDetailMessage());
                    break;
                case PURCHASE_ORDER_NUMBER:
                    purchaseOrderNumberInputLayout.setError(issue.getDetailMessage());
                    break;
                case AMOUNT:
                    amountInputLayout.setError(issue.getDetailMessage());
                    break;
                case TRADE:
                    tradeInputLayout.setError(issue.getDetailMessage());
                    break;
                case DATE:
                    purchaseDateInputLayout.setError(issue.getDetailMessage());
                    break;
                case USER_PHONE:
                    userPhoneInputLayout.setError(issue.getDetailMessage());
                    break;
                case USER_EMAIL:
                    userEmailInputLayout.setError(issue.getDetailMessage());
                    break;
                case TERMS_OF_SERVICE:
                    termsOfServiceLayout.setError(issue.getDetailMessage());
                    break;
                case PERSONAL_DATA_PROCESSING:
                    personalDataProcessingLayout.setError(issue.getDetailMessage());
                    break;
                case USE_MY_EFFIGY:
                    useMyEffigyLayout.setError(issue.getDetailMessage());
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_DATE_REQ_CODE && resultCode == Activity.RESULT_OK) {
            String date = data.getStringExtra(DatePickerFragment.EXTRA_KEY_DATE_STRING);
            purchaseDateInputLayout.setText(date);
        }

        if (requestCode == USER_DATA_FORM_REQ_CODE && resultCode == Activity.RESULT_OK) {
            boolean remember = data.getBooleanExtra(RememberUserDialogFragment.EXTRA_KEY_REMEMBER_AGREE_BOOL, false);
            boolean dontAskAgain = data.getBooleanExtra(RememberUserDialogFragment.EXTRA_KEY_REMEMBER_DONT_ASK_AGAIN_BOOL, false);

            userSettingsComponent.setDontAskAgain(dontAskAgain);

            if (remember) {
                userSettingsComponent.setUserEmail(userEmailInputLayout.getText().toString());
                userSettingsComponent.setUserPhone(userPhoneInputLayout.getText().toString());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onExceptionUi(String message) {
        toastLong(message);
        setButtonAnimator(ANIMATOR_BUTTON);
        setInteractionLocked(false);
    }

    private AddTicketRequest getValidatedInput() throws TicketValidationException {
        AddTicketRequest input = new AddTicketRequest();

        String taxRegistrationNumber = taxRegistrationNumberInputLayout.getText().toString();
        String pointOfSale = pointOfSaleInputLayout.getText().toString();
        String purchaseOrderNumber = purchaseOrderNumberInputLayout.getText().toString();
        String amount = amountInputLayout.getText().toString();
        String trade = tradeInputLayout.getText().toString();
        String date = purchaseDateInputLayout.getText().toString();
        String userPhone = userPhoneInputLayout.getText().toString();
        String userEmail = userEmailInputLayout.getText().toString();
        Boolean termsOfService = termsOfServiceLayout.isChecked();
        Boolean personalDataProcessing = personalDataProcessingLayout.isChecked();
        Boolean useMyEffigy = useMyEffigyLayout.isChecked();

        AddTicketInputValidator validator = new AddTicketInputValidator(this);
        validator.validate(
                taxRegistrationNumber,
                pointOfSale,
                purchaseOrderNumber,
                amount,
                trade,
                date,
                userPhone,
                userEmail,
                termsOfService,
                personalDataProcessing,
                useMyEffigy
        );

        input.setTaxRegistrationNumber(taxRegistrationNumber);
        input.setPointOfSale(pointOfSale);
        input.setPurchaseOrderNumber(purchaseOrderNumber);
        input.setAmount(new AmountResponse(CurrencyEnum.PLN, amount.replace(",", ".")));    //TODO 2.00
        input.setTrade(tradesAdapter.getTradeId(tradeInputLayout.getText().toString()));
        input.setDate(DateFormatter.formatDateToApi(date));
        input.setPhone(userPhone);
        input.setEmail(userEmail);
        input.setAgreements(new AgreementsRequest(termsOfService, personalDataProcessing, useMyEffigy));

        return input;
    }

    private void setInteractionLocked(final boolean locked) {
        this.handler.post(new Runnable() {

            @Override
            public void run() {
                taxRegistrationNumberInputLayout.setEnabled(!locked);
                pointOfSaleInputLayout.setEnabled(!locked);
                purchaseOrderNumberInputLayout.setEnabled(!locked);
                amountInputLayout.setEnabled(!locked);
                tradeInputLayout.setEnabled(!locked);
                purchaseDateInputLayout.setEnabled(!locked);
                userPhoneInputLayout.setEnabled(!locked);
                userEmailInputLayout.setEnabled(!locked);
                termsOfServiceLayout.setEnabled(!locked);
                personalDataProcessingLayout.setEnabled(!locked);
                useMyEffigyLayout.setEnabled(!locked);
            }
        });
    }

    private void setButtonAnimator(final int childPosition) {
        this.handler.post(new Runnable() {

            @Override
            public void run() {
                if (addTicketAnimator != null) {
                    addTicketAnimator.setDisplayedChild(childPosition);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    private void performAddTicket(final AddTicketRequest request) {
        createTicketPresenter.createTicket(request);
    }


    @Override
    public void onErrorResponse(String message) {
        Context applicationContext = getApplicationContext();

        if (applicationContext != null) {
            if (TextUtils.isEmpty(message)) {
                message = toastFailedCreateTicket;
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
        onErrorResponse(toastFailedCreateTicket);
    }

    @Override
    public void startedLoadingData() {
        setInteractionLocked(true);
        setButtonAnimator(ANIMATOR_PROGRESS);
    }

    @Override
    public void finishedLoadingData() {
        setButtonAnimator(ANIMATOR_BUTTON);
        setInteractionLocked(false);
    }

    @Override
    public void onCreateTicketFinished(AddTicketRequest request, TicketResponse ticketResponse) {

        Snackbar.make(addTicketAnimator, R.string.toast_success_created_ticket, Snackbar.LENGTH_INDEFINITE)
                .setAction(snackbarResetForm, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        resetValues();
                        if (taxRegistrationNumberInputLayout.requestFocus()) {
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                    }
                })
                .show();


        //update local adapter (TODO remove and subscribe on autocompleteDb)
        Autocomplete autocomplete = new Autocomplete(request.getTaxRegistrationNumber(), request.getPointOfSale());
        posAutocompleteAdapter.add(autocomplete);
        taxAutocompleteAdapter.add(autocomplete);
        dataManager.saveTaxAndPos(autocomplete)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Autocomplete>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "Failed to save autocomplete in database");
                    }

                    @Override
                    public void onNext(Autocomplete autocomplete) {
                        Timber.d("Saved autocomplete: '%s'", autocomplete);
                    }
                });

        if (userSettingsComponent.getDontAskAgain() == null || !userSettingsComponent.getDontAskAgain()) {
            setupRememberForm();
        }
    }

    @Override
    public void onAlreadyAddedException(ApiError apiError) {
        String message = apiError.getUserMessage();

        if (TextUtils.isEmpty(message)) {
            message = toastFailedCreateTicket;
        }
        onExceptionUi(message);
    }

    @Override
    public void onGetTradesFinished(LotteryTradesCollectionResponse lotteryTradesCollectionResponse) {
        if (lotteryTradesCollectionResponse != null || lotteryTradesCollectionResponse.getTotal() > 0) {
            tradesAdapter.updateFromCollection(lotteryTradesCollectionResponse);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

        createTicketPresenter.detachView();
        getTradesPresenter.detachView();
    }
}
