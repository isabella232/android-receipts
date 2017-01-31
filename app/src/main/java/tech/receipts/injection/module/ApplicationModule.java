package tech.receipts.injection.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import tech.receipts.injection.ApplicationContext;

/**
 * Provide application-level dependencies.
 */
@Module(includes = {
        StorageModule.class,
        SessionModule.class,
        ServiceModule.class,
        NetworkModule.class,
        AdaptersModule.class,
})
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }
}
