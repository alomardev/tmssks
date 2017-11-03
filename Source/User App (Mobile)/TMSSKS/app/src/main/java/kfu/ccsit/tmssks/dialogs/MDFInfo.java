package kfu.ccsit.tmssks.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.webkit.WebView;

import com.afollestad.materialdialogs.MaterialDialog;

import kfu.ccsit.tmssks.R;

public class MDFInfo extends BaseDialogFragment {

    public static final int TYPE_CHANGELOG = 0;
    public static final int TYPE_LICENSE = 1;

    private static final String KEY_TYPE = "type";

    private int mType;

    public MDFInfo() {
    }

    public static MDFInfo newInstance(int type) {
        MDFInfo df = new MDFInfo();

        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);

        df.setArguments(args);

        return df;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mType = getArguments().getInt(KEY_TYPE);

        WebView content = new WebView(getActivity());
        content.loadUrl(getUrl());

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(getTitle())
                .customView(content, false)
                .positiveText(R.string.dialog_positive_text_ok);

        return enhancedBuild(builder);
    }

    private String getTitle() {
        switch (mType) {
            case TYPE_CHANGELOG:
            case TYPE_LICENSE:
                return "Open Source License";
            default:
                return "";
        }
    }

    private String getUrl() {
        switch (mType) {
            case TYPE_LICENSE:
                return "file:///android_asset/htmls/open_source_license.html";
            default:
                return "";
        }
    }
}
