package kfu.ccsit.tmssks.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;

import kfu.ccsit.tmssks.R;

public class MDFProgress extends BaseDialogFragment {

    private static final String KEY_MESSAGE = "message";
    private static final String KEY_SHOW_CANCEL_BTN = "show_cancel_btn";

    public MDFProgress() {
    }

    public static MDFProgress newInstance(String message, boolean showCancelBtn) {
        MDFProgress instance = new MDFProgress();
        Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message);
        args.putBoolean(KEY_SHOW_CANCEL_BTN, showCancelBtn);
        instance.setArguments(args);
        return instance;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.content(getArguments().getString(KEY_MESSAGE))
                .progress(true, 0);
        if (getArguments().getBoolean(KEY_SHOW_CANCEL_BTN, true))
            builder.negativeText(R.string.dialog_negative_text_cancel);
        return enhancedBuild(builder);
    }
}
