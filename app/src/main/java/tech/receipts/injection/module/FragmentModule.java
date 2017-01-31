package tech.receipts.injection.module;

import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import tech.receipts.injection.PerFragment;

@Module
public class FragmentModule {
    private final Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @PerFragment
    Fragment provideFragment() {
        return fragment;
    }
}
