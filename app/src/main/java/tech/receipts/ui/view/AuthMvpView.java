package tech.receipts.ui.view;

import tech.receipts.data.api.auth.OnAuthCallback;
import tech.receipts.data.model.Auth;

public interface AuthMvpView {

    void onAuth(Auth auth);

    void onRefreshFailed(OnAuthCallback onFinishedListener);

    void onPasswordFailed();

    void onNoConnectionError();
}
