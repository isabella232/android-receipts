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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import tech.receipts.R;
import tech.receipts.ui.activity.ActivityWithOnActivityResult;

public class RememberUserDialogFragment extends AppCompatDialogFragment {

    public static final String EXTRA_KEY_REMEMBER_AGREE_BOOL = "extraKeyRememberAgree";
    public static final String EXTRA_KEY_REMEMBER_DONT_ASK_AGAIN_BOOL = "extraKeyRememberDontAskAgain";

    @BindView(R.id.user_phone_edit_text)
    protected MaterialEditText userPhoneInputLayout;

    @BindView(R.id.user_email_edit_text)
    protected MaterialEditText userEmailInputLayout;

    @BindString(R.string.remember_form_title)
    protected String rememberFormTitle;

    @BindString(R.string.cancel)
    protected String cancel;

    @BindString(R.string.ok)
    protected String ok;

    @Inject
    public RememberUserDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_remember_user_data, null, true);
        ButterKnife.bind(this, activity);

        final TextView rememberEmail = (TextView) view.findViewById(R.id.remember_email);
        rememberEmail.setText(userEmailInputLayout.getText().toString());
        final TextView rememberPhone = (TextView) view.findViewById(R.id.remember_phone);
        rememberPhone.setText(userPhoneInputLayout.getText().toString());
        final CheckBox dontAskAgain = (CheckBox) view.findViewById(R.id.checkbox_remember_dont_ask_again);

        return new AlertDialog.Builder(activity, R.style.DialogTheme)
                .setTitle(rememberFormTitle)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                remember(activity, true, true);
                            }
                        }
                )
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                remember(activity, dontAskAgain.isChecked(), false);
                            }
                        }
                )
                .setView(view)
                .create();
    }

    private void remember(Activity activity, boolean dontAskAgain, boolean answer) {
        Intent i = new Intent();
        i.putExtra(EXTRA_KEY_REMEMBER_AGREE_BOOL, answer);
        i.putExtra(EXTRA_KEY_REMEMBER_DONT_ASK_AGAIN_BOOL, dontAskAgain);

        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        } else {
            ((ActivityWithOnActivityResult) activity).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        }
    }
}