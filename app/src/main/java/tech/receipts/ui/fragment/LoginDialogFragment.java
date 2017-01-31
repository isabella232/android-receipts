package tech.receipts.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import tech.receipts.R;
import tech.receipts.TicketsLotteryApp;
import tech.receipts.components.UserSettingsComponent;
import tech.receipts.ui.activity.ActivityWithOnActivityResult;

public class LoginDialogFragment extends AppCompatDialogFragment {

    public static final String EXTRA_KEY_LOGIN_STRING = "extraKeyLogin";
    public static final String EXTRA_KEY_PASSWORD_STRING = "extraKeyPassword";
    public static final String EXTRA_KEY_STORE_BOOL = "extraKeyStore";

    @Inject
    protected UserSettingsComponent userSettingsComponent;

    @BindView(R.id.login_edit_text)
    protected MaterialEditText loginEditText;

    @BindView(R.id.password_edit_text)
    protected MaterialEditText passwordEditText;

    @BindView(R.id.checkbox_store_user_data)
    protected CheckBox checkboxStoreUserData;

    @BindString(R.string.login_form_title)
    protected String loginFormTitle;

    @BindString(R.string.cancel)
    protected String cancel;

    @BindString(R.string.ok)
    protected String ok;

    @Inject
    public LoginDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_login, null, true);
        ButterKnife.bind(this, view);

        ((TicketsLotteryApp) activity.getApplicationContext()).component().inject(this);

        String userEmail = userSettingsComponent.getUserEmail();
        if (!TextUtils.isEmpty(userEmail)) {
            loginEditText.setText(userEmail);
        }

        String userLogin = userSettingsComponent.getUserLogin();
        if (!TextUtils.isEmpty(userLogin)) {
            loginEditText.setText(userLogin);
        }

        String userPassword = userSettingsComponent.getUserPassword();
        if (!TextUtils.isEmpty(userPassword)) {
            passwordEditText.setText(userPassword);
        }

        return new AlertDialog.Builder(activity, R.style.DialogTheme)
                .setTitle(loginFormTitle)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                saveLoginData(activity);
                            }
                        }
                )
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .setView(view)
                .create();
    }

    private void saveLoginData(Activity activity) {
        Intent i = new Intent();
        i.putExtra(EXTRA_KEY_LOGIN_STRING, loginEditText.getText().toString());
        i.putExtra(EXTRA_KEY_PASSWORD_STRING, passwordEditText.getText().toString());
        i.putExtra(EXTRA_KEY_STORE_BOOL, checkboxStoreUserData.isChecked());

        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        } else {
            ((ActivityWithOnActivityResult) activity).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        }

    }
}