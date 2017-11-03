package kfu.ccsit.tmssks.prefs;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.DialogAction;

import kfu.ccsit.tmssks.R;
import kfu.ccsit.tmssks.dialogs.BaseDialogFragment;
import kfu.ccsit.tmssks.dialogs.MDFListOptions;

public class ListPreference extends BaseDialogFragmentPreference {

    private String mTitle;
    private String mCurrentValue;
    private String mDefaultValue;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;

    public ListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomPreferences);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.CustomPreferences_entries:
                    mEntries = a.getTextArray(index);
                    break;
                case R.styleable.CustomPreferences_entryValues:
                    mEntryValues = a.getTextArray(index);
                    break;
                case R.styleable.CustomPreferences_dialogTitle:
                    mTitle = a.getString(index);
            }
        }
        a.recycle();
    }

    @Override
    protected BaseDialogFragment createDialogFragment() {
        return MDFListOptions.newInstance(mTitle, mEntries, mEntryValues, mCurrentValue);
    }

    @Override
    public void onDialogButtonClick(DialogAction button) {
        if (button == DialogAction.POSITIVE) {
            mCurrentValue = ((MDFListOptions) getDialogFragment()).getCurrentValue();
            persistString(mCurrentValue);
            callChangeListener(mCurrentValue);
        }
    }

    public CharSequence[] getEntries() {
        return mEntries;
    }

    public CharSequence[] getEntryValues() {
        return mEntryValues;
    }

    // Initializing the current value
    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
        if (restorePersistedValue) {
            mCurrentValue = getPersistedString(mDefaultValue);
        } else {
            mCurrentValue = (String) defaultValue;
            persistString(mCurrentValue);
        }
    }

    // Providing a default value
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        mDefaultValue = a.getText(index).toString();
        return mDefaultValue;
    }
}
