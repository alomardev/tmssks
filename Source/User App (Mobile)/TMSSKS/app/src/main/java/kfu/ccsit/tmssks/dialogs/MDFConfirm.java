package kfu.ccsit.tmssks.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;

import kfu.ccsit.tmssks.R;

public class MDFConfirm extends BaseDialogFragment {

    private static final String KEY_MESSAGE = "message";

    public MDFConfirm() {
    }

    public static MDFConfirm newInstance(String message) {
        MDFConfirm instance = new MDFConfirm();
        Bundle args = new Bundle();
        args.putString(KEY_MESSAGE, message);
        instance.setArguments(args);
        return instance;
    }

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.content(getArguments().getString(KEY_MESSAGE))
                .positiveText(R.string.confirm);
        return enhancedBuild(builder);
    }
}
