package kfu.ccsit.tmssks;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_USERNAME = "user_username";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_DONE_NOTIFY = "done_notify";
    public static final String KEY_DONE_ALERTS = "done_alerts";

    public static SharedPreferences getDefault(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
