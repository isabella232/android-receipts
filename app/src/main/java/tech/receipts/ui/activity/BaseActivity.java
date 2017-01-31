package tech.receipts.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tech.receipts.TicketsLotteryApp;
import tech.receipts.injection.component.ActivityComponent;
import tech.receipts.injection.component.DaggerActivityComponent;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent component() {

        if (component == null) {
            component = DaggerActivityComponent.builder()
                    .applicationComponent(TicketsLotteryApp.get(this).component())
                    .build();
        }

        return component;
    }

}
