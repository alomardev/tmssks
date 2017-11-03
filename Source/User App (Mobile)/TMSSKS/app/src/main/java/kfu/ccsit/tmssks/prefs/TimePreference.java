package kfu.ccsit.tmssks.prefs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.DialogAction;

import kfu.ccsit.tmssks.R;
import kfu.ccsit.tmssks.dialogs.BaseDialogFragment;
import kfu.ccsit.tmssks.dialogs.MDFTimePicker;

public class TimePreference extends BaseDialogFragmentPreference {

    private String mTitle, mCurrentValue, mDefaultValue;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDefaultValue = "0000";
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomPreferences);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.CustomPreferences_dialogTitle:
                    mTitle = a.getString(index);
            }
        }
        a.recycle();
    }

    @Override
    protected BaseDialogFragment createDialogFragment() {
        return MDFTimePicker.newInstance(mTitle, mCurrentValue);
    }

    @Override
    public void onDialogButtonClick(DialogAction button) {
        if (button == DialogAction.POSITIVE) {
            mCurrentValue = ((MDFTimePicker) getDialogFragment()).getCurrentValue();
            persistString(mCurrentValue);
            callChangeListener(mCurrentValue);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        if (restorePersistedValue) {
            mCurrentValue = getPersistedString(mDefaultValue);
        } else {
            mCurrentValue = ((String) defaultValue);
            persistString(mCurrentValue);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        mDefaultValue = a.getString(index);
        return mDefaultValue;
    }

    public static int getHours(String value) {
        return Integer.parseInt(value.substring(0, 2));
    }

    public static int getMinutes(String value) {
        return Integer.parseInt(value.substring(2));
    }
}
