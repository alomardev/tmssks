package kfu.ccsit.tmssks;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;

import kfu.ccsit.tmssks.dialogs.MDFInfo;

public class MiscUtils {

    public static AppCompatActivity castActivity(Context context) {
        if (context instanceof ContextThemeWrapper) {
            return castActivity(((ContextThemeWrapper) context).getBaseContext());
        } else if (context instanceof AppCompatActivity) {
            return (AppCompatActivity) context;
        }
        return null;
    }

    public static void contactDeveloper(Context context) {
        context.startActivity(Intent.createChooser(
                new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@tmssks.com?subject=TMSSKS Feedback")), "Send Feedback"));
    }

    public static MDFInfo showLicenseDialog(AppCompatActivity activity) {
        MDFInfo dialog = MDFInfo.newInstance(MDFInfo.TYPE_LICENSE);
        dialog.show(activity.getSupportFragmentManager(), "license_dialog");
        return dialog;
    }
}
