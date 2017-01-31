package tech.receipts.ui.activity;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import tech.receipts.R;
import tech.receipts.components.DayNightMode;
import tech.receipts.components.UserSettingsComponent;
import tech.receipts.data.database.AutocompleteDb;

public class OptionsActivity extends AppCompatPreferenceActivity {

    @Inject
    protected UserSettingsComponent userSettingsComponent;

    @Inject
    protected AutocompleteDb autocompleteDb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        addPreferencesFromResource(R.xml.preferences);
        component().inject(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        bar.setHomeButtonEnabled(true);
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowTitleEnabled(true);
        bar.setTitle(R.string.title_section_options);

        setUpClearUserData();
        setUpClearAutocomplete();
        setUpDefaultDate();
        setUpDaynightMode();
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return OptionsActivity.class.getName().equals(fragmentName);
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

    private void setUpClearUserData() {
        final Preference clearUserData = findPreference(getString(R.string.clear_user_data));

        if (clearUserData != null) {
            clearUserData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    userSettingsComponent.clearUserData();
                    clearUserData.setEnabled(false);
                    return true;
                }
            });

            boolean isSavedData = userSettingsComponent.getUserLogin() != null
                    || userSettingsComponent.getDontAskAgain() != null
                    || userSettingsComponent.getUserPhone() != null;
            clearUserData.setEnabled(isSavedData);
        }
    }

    private void setUpClearAutocomplete() {
        final Preference clearAutocomplete = findPreference(getString(R.string.clear_autocomplete));

        if (clearAutocomplete != null) {
            clearAutocomplete.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    autocompleteDb.clearAutocomplete();
                    clearAutocomplete.setEnabled(false);
                    return true;
                }
            });

            clearAutocomplete.setEnabled(autocompleteDb.getPosCount() > 0);
        }
    }

    private void setUpDaynightMode() {
        final Preference daynightMode = findPreference(getString(R.string.select_daynight_mode));

        if (daynightMode != null) {

            if (daynightMode instanceof ListPreference) {

                ListPreference list = (ListPreference) daynightMode;

                int count = DayNightMode.values().length - 1;   //without unknown
                CharSequence[] entryValues = new CharSequence[count];
                CharSequence[] entries = new CharSequence[count];
                int i = 0;

                for (DayNightMode mode : DayNightMode.values()) {

                    if (!mode.equals(DayNightMode.UNKNOWN)) {
                        entries[i] = getResources().getText(mode.getResourceId());
                        entryValues[i] = String.valueOf(mode.getValue());
                        ++i;
                    }
                }

                DayNightMode defaultEnum = userSettingsComponent.getDefaultDayNightMode();
                list.setDefaultValue(defaultEnum.getValue());
                list.setEntryValues(entryValues);
                list.setEntries(entries);

                list.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        userSettingsComponent.setDayNightMode(Integer.parseInt(newValue.toString()));
                        userSettingsComponent.handleDayNightMode(null);
                        return true;
                    }
                });
            }
        }
    }

    private void setUpDefaultDate() {
        final Preference defaultDate = findPreference(getString(R.string.set_default_date));

        if (defaultDate != null) {
            defaultDate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (preference instanceof CheckBoxPreference) {
                        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                        userSettingsComponent.setDefaultCurrentDate(checkBoxPreference.isChecked());
                    }
                    return true;
                }
            });
        }
    }
}
