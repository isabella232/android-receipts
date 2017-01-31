package tech.receipts.components;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import javax.inject.Inject;

import timber.log.Timber;

public class UserSettingsComponent {

    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String DONT_ASK_AGAIN = "dontAskAgain";
    public static final String DEFAULT_CURRENT_DATE = "defaultCurrentDate";
    public static final String DAY_NIGHT_MODE = "dayNightMode";

    private final SharedPreferences sharedPreferences;

    @Inject
    public UserSettingsComponent(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public String getUserLogin() {
        return sharedPreferences.getString(LOGIN, null);
    }

    public String getUserPassword() {
        return sharedPreferences.getString(PASSWORD, null);
    }

    public String getUserPhone() {
        return sharedPreferences.getString(PHONE, null);
    }

    public String getUserEmail() {
        return sharedPreferences.getString(EMAIL, null);
    }

    public Boolean getDefaultCurrentDate() {
        return sharedPreferences.getBoolean(DEFAULT_CURRENT_DATE, true);
    }

    public Integer getDayNightMode() {
        DayNightMode defaultDayNightMode = getDefaultDayNightMode();
        return sharedPreferences.getInt(DAY_NIGHT_MODE, defaultDayNightMode.getValue());
    }

    public Boolean getDontAskAgain() {
        if (sharedPreferences.contains(DONT_ASK_AGAIN)) {
            return sharedPreferences.getBoolean(DONT_ASK_AGAIN, false);
        }

        return null;
    }

    public void setUserLogin(String login) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN, login);
        editor.apply();
    }

    public void setUserPassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public void setUserEmail(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public void setUserPhone(String phone) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PHONE, phone);
        editor.apply();
    }

    public void setDontAskAgain(boolean dontAskAgain) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DONT_ASK_AGAIN, dontAskAgain);
        editor.apply();
    }

    public void clearUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void setDayNightMode(Integer mode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(DAY_NIGHT_MODE, mode);
        editor.apply();
    }

    public void setDefaultCurrentDate(boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(DEFAULT_CURRENT_DATE, value);
        editor.apply();
    }

    public DayNightMode getDefaultDayNightMode() {
        DayNightMode defaultEnum = DayNightMode.DAY;

        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                defaultEnum = DayNightMode.AUTO;
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                defaultEnum = DayNightMode.DAY;
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                defaultEnum = DayNightMode.NIGHT;
                break;
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                defaultEnum = DayNightMode.FOLLOW_SYSTEM;
                break;
            default:
                defaultEnum = DayNightMode.DAY;
        }

        return defaultEnum;
    }

    public boolean handleDayNightMode(AppCompatDelegate delegate) {

        DayNightMode defaultDayNightMode = getDefaultDayNightMode();
        Integer value = getDayNightMode();
        DayNightMode dayNightMode = DayNightMode.get(value);

        if (dayNightMode.equals(defaultDayNightMode)) {
            Timber.d("skip handleDayNightMode: the same");
            return false;
        }

        switch (dayNightMode) {
            case AUTO:
                if (delegate != null) {
                    delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                break;
            case DAY:
                if (delegate != null) {
                    delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case NIGHT:
                if (delegate != null) {
                    delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case FOLLOW_SYSTEM:
                if (delegate != null) {
                    delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }

        return true;
    }
}
