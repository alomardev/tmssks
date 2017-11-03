package kfu.ccsit.tmssks.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import kfu.ccsit.tmssks.R;

public class MDFListOptions extends BaseDialogFragment implements MaterialDialog.ListCallbackSingleChoice {

    private static final String KEY_TITLE = "title";
    private static final String KEY_ENTRIES = "entries";
    private static final String KEY_VALUES = "values";
    private static final String KEY_SELECTED_VALUE = "selected_value";

    private CharSequence[] mValues;
    private String mSelectedValue;

    public static MDFListOptions newInstance(String title, CharSequence[] entries,
                                             CharSequence[] values, String selectedValue) {
        MDFListOptions instance = new MDFListOptions();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putCharSequenceArray(KEY_ENTRIES, entries);
        args.putCharSequenceArray(KEY_VALUES, values);
        args.putString(KEY_SELECTED_VALUE, selectedValue);
        instance.setArguments(args);
        return instance;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CharSequence[] entries = getArguments().getCharSequenceArray(KEY_ENTRIES);
        mValues = getArguments().getCharSequenceArray(KEY_VALUES);
        mSelectedValue = getArguments().getString(KEY_SELECTED_VALUE);

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        // Getting the index
        int index = -1;
        if (mSelectedValue != null)
            for (int i = 0; i < entries.length; i++)
                if (mSelectedValue.equals(mValues[i].toString()))
                    index = i;

        builder.items(entries).title(getArguments().getString(KEY_TITLE))
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(index, this)
                .positiveText(R.string.dialog_positive_text_ok)
                .negativeText(R.string.dialog_negative_text_cancel);

        return enhancedBuild(builder);
    }

    @Override
    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        mSelectedValue = (String) mValues[which];
        return true;
    }

    public String getCurrentValue() {
        return mSelectedValue;
    }
}
