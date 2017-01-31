package tech.receipts.injection.component;

import android.support.v4.app.Fragment;

import dagger.Component;
import tech.receipts.injection.PerFragment;
import tech.receipts.injection.module.FragmentModule;
import tech.receipts.ui.fragment.FinishResultsFragment;

@PerFragment
@Component(
        dependencies = ActivityComponent.class,
        modules = FragmentModule.class
)
public interface FragmentComponent {
    Fragment fragment();

    void inject(FinishResultsFragment fragment);

}

