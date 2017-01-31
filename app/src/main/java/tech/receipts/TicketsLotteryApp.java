package tech.receipts;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.crashlytics.android.Crashlytics;
import com.frogermcs.androiddevmetrics.AndroidDevMetrics;

import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import tech.receipts.components.NetworkStateManager;
import tech.receipts.components.UserSettingsComponent;
import tech.receipts.injection.component.ApplicationComponent;
import tech.receipts.injection.component.DaggerApplicationComponent;
import tech.receipts.injection.module.ApplicationModule;
import timber.log.Timber;

public class TicketsLotteryApp extends Application {
    private ApplicationComponent component;

    @Inject
    protected NetworkStateManager networkStateManager;

    @Inject
    protected UserSettingsComponent userSettingsComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        component();
        configureFabric();
        configureStrictMode();
        configureLogging();
        configureAndroidDevMetrics();
//        configureInjection();
//        checkConnection();

        userSettingsComponent.handleDayNightMode(null);
    }

    private void configureAndroidDevMetrics() {
        if (BuildConfig.DEBUG) {
            AndroidDevMetrics.initWith(this);
        }
    }
//
//    private void checkConnection() {
//        if (!networkStateManager.isConnectedOrConnecting()) {
//            Timber.d("no internet connection");
//            CharSequence message = getResources().getText(R.string.toast_no_connection);
//            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//        }
//    }

    private void configureFabric() {
        if (BuildConfig.USE_CRASHLYTICS) {
            Fabric.with(this, new Crashlytics());
        }
    }

    private void configureLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void configureStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().penaltyDeathOnNetwork().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().detectActivityLeaks().penaltyLog().build());
    }

    public static TicketsLotteryApp get(Context context) {
        return (TicketsLotteryApp) context.getApplicationContext();
    }

    public ApplicationComponent component() {
        if (component == null) {
            component = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }

        component.inject(this);
        return component;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.component = applicationComponent;
    }
}
