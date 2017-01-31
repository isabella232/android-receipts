package tech.receipts.ui.activity;

import android.content.Intent;

public interface ActivityWithOnActivityResult {

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
