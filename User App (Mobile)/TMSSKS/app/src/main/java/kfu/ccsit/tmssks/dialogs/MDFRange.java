package kfu.ccsit.tmssks.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import kfu.ccsit.tmssks.R;

public class MDFRange extends BaseDialogFragment {

    private static final String KEY_TITLE = "title";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_PATTERN = "pattern";
    private static final String KEY_MIN = "min";
    private static final String KEY_MAX = "max";
    private static final String KEY_VALUE = "value";

    private int mMin, mCurrentValue;
    private String mValuePattern;

    private TextView mValueTv;
    private SeekBar mSeekBar;

    public static MDFRange newInstance(String title, String message, String valuePattern,
                                       int min, int max, int value) {
        MDFRange instance = new MDFRange();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putString(KEY_MESSAGE, message);
        args.putString(KEY_PATTERN, valuePattern);
        args.putInt(KEY_MIN, min);
        args.putInt(KEY_MAX, max);
        args.putInt(KEY_VALUE, value);
        instance.setArguments(args);
        return instance;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mValuePattern = getArguments().getString(KEY_PATTERN);
        if (mValuePattern == null)
            mValuePattern = "%d";
        else {
            mValuePattern = mValuePattern.replace("%", "%%").replace("[n]", "%d");
        }

        String title = getArguments().getString(KEY_TITLE);
        String message = getArguments().getString(KEY_MESSAGE);
        int max = getArguments().getInt(KEY_MAX);
        mMin = getArguments().getInt(KEY_MIN);
        mCurrentValue = getArguments().getInt(KEY_VALUE);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        MaterialDialog dialog;

        builder.title(title).positiveText(R.string.dialog_positive_text_ok)
                .negativeText(R.string.dialog_negative_text_cancel)
                .customView(R.layout.dialog_range, false);

        dialog = enhancedBuild(builder);

        final View root = dialog.getCustomView();
        TextView messageTv = (TextView) root.findViewById(R.id.tv_message);
        mSeekBar = (SeekBar) root.findViewById(R.id.seek_bar);
        mValueTv = (TextView) root.findViewById(R.id.tv_value);

        if (message == null || message.isEmpty())
            messageTv.setVisibility(View.GONE);
        else
            messageTv.setText(message);

        // Getting the current value
        mSeekBar.setMax(max - mMin);
        mSeekBar.setProgress(mCurrentValue - mMin);

        updateDialog();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateDialog();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        return dialog;
    }

    private void updateDialog() {
        mCurrentValue = mMin + mSeekBar.getProgress();
        mValueTv.setText(String.format(mValuePattern, mCurrentValue));
    }

    public int getCurrentValue() {
        return mCurrentValue;
    }
}
