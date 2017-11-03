package kfu.ccsit.tmssks.prefs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.DialogAction;
import kfu.ccsit.tmssks.R;
import kfu.ccsit.tmssks.dialogs.BaseDialogFragment;
import kfu.ccsit.tmssks.dialogs.MDFRange;

public class RangePreference extends BaseDialogFragmentPreference {

    private int mMin, mMax, mCurrentValue, mDefaultValue;
    private String mTitle, mMessage, mValuePattern;

    public RangePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Default values
        mMin = 0;
        mMax = 100;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomPreferences);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.CustomPreferences_min:
                    mMin = a.getInt(index, mMin);
                    break;
                case R.styleable.CustomPreferences_max:
                    mMax = a.getInt(index, mMax);
                    break;
                case R.styleable.CustomPreferences_valuePattern:
                    mValuePattern = a.getString(index);
                    break;
                case R.styleable.CustomPreferences_dialogMessage:
                    mMessage = a.getString(index);
                    break;
                case R.styleable.CustomPreferences_dialogTitle:
                    mTitle = a.getString(index);
            }
        }
        a.recycle();
    }

    @Override
    protected BaseDialogFragment createDialogFragment() {
        return MDFRange.newInstance(mTitle, mMessage, mValuePattern, mMin, mMax, mCurrentValue);
    }

    @Override
    public void onDialogButtonClick(DialogAction button) {
        if (button == DialogAction.POSITIVE) {
            mCurrentValue = ((MDFRange) getDialogFragment()).getCurrentValue();
            persistInt(mCurrentValue);
            callChangeListener(mCurrentValue);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        if (restorePersistedValue)
            mCurrentValue = getPersistedInt(mDefaultValue);
        else {
            mCurrentValue = (int) defaultValue;
            persistInt(mCurrentValue);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        mDefaultValue = a.getInt(index, mDefaultValue);
        return mDefaultValue;
    }
}
