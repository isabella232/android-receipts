package tech.receipts.injection.module;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tech.receipts.components.ObscuredSharedPreferences;

@Module
public class StorageModule {

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return new ObscuredSharedPreferences(
                application, PreferenceManager.getDefaultSharedPreferences(application)
        );
    }
}
