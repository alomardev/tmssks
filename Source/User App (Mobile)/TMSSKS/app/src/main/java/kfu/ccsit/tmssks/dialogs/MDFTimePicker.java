package kfu.ccsit.tmssks.dialogs;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.MaterialDialog;

import kfu.ccsit.tmssks.R;
import kfu.ccsit.tmssks.TimeUtils;

public class MDFTimePicker extends BaseDialogFragment implements TimePicker.OnTimeChangedListener {

    private static final String KEY_TITLE = "title";
    private static final String KEY_FORMATTED_TIME = "formatted_time";

    private String mCurrentValue;

    public static MDFTimePicker newInstance(String title, String formattedTime) {
        MDFTimePicker instance = new MDFTimePicker();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putString(KEY_FORMATTED_TIME, formattedTime);
        instance.setArguments(args);
        return instance;
    }

    @SuppressWarnings({"ConstantConditions", "deprecation"})
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        MaterialDialog dialog;
        TimePicker timePicker;

        builder.title(getArguments().getString(KEY_TITLE))
                .customView(R.layout.dialog_time_picker, false)
                .positiveText(R.string.done)
                .negativeText(R.string.dialog_negative_text_cancel);

        dialog = enhancedBuild(builder);

        timePicker = (TimePicker) dialog.getCustomView();

        mCurrentValue = getArguments().getString(KEY_FORMATTED_TIME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(TimeUtils.getFormattedTimeHour(mCurrentValue));
            timePicker.setMinute(TimeUtils.getFormattedTimeMinute(mCurrentValue));
        } else {
            timePicker.setCurrentHour(TimeUtils.getFormattedTimeHour(mCurrentValue));
            timePicker.setCurrentMinute(TimeUtils.getFormattedTimeMinute(mCurrentValue));
        }
        timePicker.setOnTimeChangedListener(this);

        return dialog;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        mCurrentValue = TimeUtils.getFormattedTime(hourOfDay, minute);
    }

    public String getCurrentValue() {
        return mCurrentValue;
    }
}
