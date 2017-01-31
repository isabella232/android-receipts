package tech.receipts.injection.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;
import tech.receipts.TicketsLotteryApp;
import tech.receipts.adapter.ResultsTabsAdapter;
import tech.receipts.adapter.TradesAdapter;
import tech.receipts.components.NetworkStateManager;
import tech.receipts.components.autocomplete.PosAutocompleteAdapter;
import tech.receipts.components.autocomplete.TaxAutocompleteAdapter;
import tech.receipts.data.AuthProvider;
import tech.receipts.data.DataManager;
import tech.receipts.data.api.auth.AuthApiService;
import tech.receipts.data.api.caller.CreateTicketCaller;
import tech.receipts.data.api.caller.GetTradesCaller;
import tech.receipts.data.api.caller.TicketsCaller;
import tech.receipts.data.database.AutocompleteDb;
import tech.receipts.data.model.Auth;
import tech.receipts.injection.ApplicationContext;
import tech.receipts.injection.module.ApplicationModule;
import tech.receipts.ui.fragment.LoginDialogFragment;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();

    Application application();

    SharedPreferences sharedPreferences();

    DataManager dataManager();

    LayoutInflater layoutInflater();

    ConnectivityManager connectivityManager();

    NetworkStateManager networkStateManager();

    AuthProvider sessionHandler();

    AutocompleteDb autocompleteDb();

    Auth auth();

    TradesAdapter tradesAdapter();

    Retrofit retrofit();

    AuthApiService authApiService();

    void inject(TicketsLotteryApp ticketsLotteryApp);

    void inject(CreateTicketCaller delegate);

    void inject(TicketsCaller ticketsPresenter);

    void inject(AuthProvider authProvider);

    void inject(ResultsTabsAdapter resultsTabsAdapter);

    void inject(PosAutocompleteAdapter posAutocompleteAdapter);

    void inject(TaxAutocompleteAdapter taxAutocompleteAdapter);

    void inject(LoginDialogFragment loginDialogFragment);

    void inject(GetTradesCaller getTradesPresenter);

}
