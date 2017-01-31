package tech.receipts.injection.component;

import dagger.Component;
import tech.receipts.components.autocomplete.PosAutocompleteAdapter;
import tech.receipts.components.autocomplete.TaxAutocompleteAdapter;
import tech.receipts.injection.PerActivity;
import tech.receipts.injection.module.ActivityModule;
import tech.receipts.ui.activity.AboutActivity;
import tech.receipts.ui.activity.MainActivity;
import tech.receipts.ui.activity.OptionsActivity;
import tech.receipts.ui.activity.ResultsActivity;
import tech.receipts.ui.activity.TicketsActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(OptionsActivity optionsActivity);

    void inject(TicketsActivity ticketsActivity);
    
    void inject(MainActivity mainActivity);

    void inject(ResultsActivity resultsActivity);

    void inject(PosAutocompleteAdapter posAutocompleteAdapter);

    void inject(TaxAutocompleteAdapter taxAutocompleteAdapter);

    void inject(AboutActivity aboutActivity);
}
