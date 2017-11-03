package kfu.ccsit.tmssks.prefs;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.DialogAction;

import kfu.ccsit.tmssks.ActivityBase;
import kfu.ccsit.tmssks.R;
import kfu.ccsit.tmssks.dialogs.BaseDialogFragment;

public abstract class BaseDialogFragmentPreference extends Preference implements
        BaseDialogFragment.DialogFragmentListener {

    private CharSequence mDialogTitle, mPositiveText, mNegativeText;

    private BaseDialogFragment mDialogFrag;

    public BaseDialogFragmentPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDialogTitle = "title";
        mPositiveText = "OK";
        mNegativeText = "Cancel";

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomPreferences);
        for (int i = a.getIndexCount(); i >= 0; i--) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomPreferences_dialogTitle:
                    mDialogTitle = a.getText(attr);
                    break;
                case R.styleable.CustomPreferences_positiveButtonText:
                    mPositiveText = a.getText(attr);
                    break;
                case R.styleable.CustomPreferences_negativeButtonText:
                    mNegativeText = a.getText(attr);
                    break;
            }
        }
        a.recycle();

        mDialogFrag = (BaseDialogFragment)
                ((ActivityBase) context).getSupportFragmentManager().findFragmentByTag(getKey());
        if (mDialogFrag != null)
            mDialogFrag.setDialogFragmentListener(this);
    }

    @Override
    protected void onClick() {
        getDialogFragment().show(((ActivityBase) getContext()).getSupportFragmentManager(),
                getKey());
    }

    public BaseDialogFragment getDialogFragment() {

        mDialogFrag = (BaseDialogFragment) ((AppCompatActivity) getContext())
                .getSupportFragmentManager().findFragmentByTag(getKey());

        if (mDialogFrag == null) {
            mDialogFrag = createDialogFragment();
            mDialogFrag.setDialogFragmentListener(this);
        }

        return mDialogFrag;
    }

    public CharSequence getDialogTitle() {
        return mDialogTitle != null ? mDialogTitle : "";
    }

    public CharSequence getPositiveText() {
        return mPositiveText != null ? mPositiveText : "";
    }

    public CharSequence getNegativeText() {
        return mNegativeText != null ? mNegativeText : "";
    }

    @Override
    public void onDialogDismiss(BaseDialogFragment dialog, String tag) {
        onDialogDismiss();
    }

    @Override
    public void onDialogButtonClick(BaseDialogFragment dialog, String tag, DialogAction button) {
        onDialogButtonClick(button);
        dialog.dismiss();
    }

    public void onDialogDismiss() {
    }

    public void onDialogButtonClick(DialogAction button) {
    }

    protected abstract BaseDialogFragment createDialogFragment();
}
