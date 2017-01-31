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
import android.widget.DatePicker;

import com.rengwuxian.materialedittext.MaterialEditText;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import tech.receipts.R;
import tech.receipts.components.DateFormatter;
import tech.receipts.ui.activity.ActivityWithOnActivityResult;
import timber.log.Timber;

public class DatePickerFragment extends AppCompatDialogFragment {

    public static final String EXTRA_KEY_DATE_STRING = "extraKeyDay";
    public static final String FORMATTED_DATE = "formattedDate";

    @BindString(R.string.input_purchase_date)
    protected String inputPurchaseDate;

    @BindString(R.string.cancel)
    protected String cancel;

    @BindString(R.string.ok)
    protected String ok;

    @BindView(R.id.purchase_date_edit_text)
    protected MaterialEditText purchaseDateInputLayout;

    private DatePicker datePicker;
    private String selectedDate;

    @Inject
    public DatePickerFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Timber.d("onSaveInstanceState: %s", savedInstanceState);

        if (savedInstanceState != null) {
            String selectedDate = getFormattedDate();
            Timber.d("onSaveInstanceState save: %s", selectedDate);
            savedInstanceState.putString(FORMATTED_DATE, selectedDate);
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        ButterKnife.bind(this, activity);

        LayoutInflater inflater = LayoutInflater.from(activity);
        datePicker = (DatePicker) inflater.inflate(R.layout.date_picker, null, false);

        if (savedInstanceState != null) {
            String savedInstanceStateString = savedInstanceState.getString(FORMATTED_DATE);
            if (savedInstanceStateString != null) {
                selectedDate = savedInstanceStateString;
                Timber.d("onCreate restored savedInstanceState: %s", selectedDate);
            } else {
                Timber.d("onCreate nothing to restore from bundle");
            }
        }

        String date = purchaseDateInputLayout.getText().toString();

        if (!TextUtils.isEmpty(selectedDate)) {
            date = selectedDate;
        }

        try {
            LocalDate inputDate = LocalDate.parse(date, DateFormatter.DATE_FORMATTER);

            if (inputDate != null) {
                datePicker.updateDate(inputDate.getYear(), inputDate.getMonthOfYear() - 1, inputDate.getDayOfMonth());
            }
        } catch (IllegalArgumentException ex) {
            Timber.i(ex.getMessage());
        }

        datePicker.setMinDate(DateTime.now().withDayOfMonth(1).withMinuteOfHour(0).withHourOfDay(0).withSecondOfMinute(0).getMillis());
        datePicker.setMaxDate(DateTime.now().dayOfMonth().withMaximumValue().withMinuteOfHour(59).withHourOfDay(23).withSecondOfMinute(59).getMillis());

        return new AlertDialog.Builder(activity, R.style.DialogTheme)
                .setTitle(inputPurchaseDate)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                returnDate(activity);
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
                .setView(datePicker)
                .create();
    }

    private void returnDate(Activity activity) {
        Intent i = new Intent();

        i.putExtra(EXTRA_KEY_DATE_STRING, getFormattedDate());

        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        } else {
            ((ActivityWithOnActivityResult) activity).onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
        }
    }

    private String getFormattedDate() {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();

        StringBuilder sb = new StringBuilder();

        sb.append(day < 10 ? "0" : "");
        sb.append(day);
        sb.append(".");
        sb.append(month < 10 ? "0" : "");
        sb.append(month);
        sb.append(".");
        sb.append(year);

        return sb.toString();
    }

}