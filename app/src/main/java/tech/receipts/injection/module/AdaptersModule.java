package tech.receipts.injection.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tech.receipts.adapter.TradesAdapter;

@Module
public class AdaptersModule {

    @Provides
    @Singleton
    TradesAdapter provideTradesAdapter(Application application) {
        return new TradesAdapter(application.getApplicationContext());
    }
}
