package tech.receipts.data.api.auth;

import tech.receipts.data.model.Auth;

public interface OnAuthCallback {
    void onSuccess(Auth response);

    void onError();

}
