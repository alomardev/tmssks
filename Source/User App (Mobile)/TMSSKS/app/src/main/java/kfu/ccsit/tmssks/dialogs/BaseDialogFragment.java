package kfu.ccsit.tmssks.dialogs;

import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import kfu.ccsit.tmssks.R;

public class BaseDialogFragment extends DialogFragment implements
        MaterialDialog.SingleButtonCallback {

    public interface DialogFragmentListener {
        void onDialogDismiss(BaseDialogFragment dialog, String tag);

        void onDialogButtonClick(BaseDialogFragment dialog, String tag, DialogAction button);
    }

    private DialogFragmentListener mCallback;

    public BaseDialogFragment() {
    }

    public MaterialDialog enhancedBuild(MaterialDialog.Builder builder) {
        builder.onPositive(this).onNegative(this).onNeutral(this);

        MaterialDialog dialog = builder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            dialog.getTitleView().setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
        }
        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        return dialog;
    }

    public void setDialogFragmentListener(DialogFragmentListener callback) {
        mCallback = callback;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mCallback != null)
            mCallback.onDialogDismiss(this, getTag());
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        if (mCallback != null)
            mCallback.onDialogButtonClick(this, getTag(), which);
    }

    public void callDialogButtonClick(DialogAction which) {
        onClick((MaterialDialog) getDialog(), which);
    }
}
