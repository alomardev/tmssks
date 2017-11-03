package kfu.ccsit.tmssks;

import android.util.Log;

public class LogUtils {

    private static final boolean ENABLE = true;
    private static final String TAG = "TMSSKS";

    public static void d(String message) {
        if (ENABLE) {
            Log.d(TAG, message);
        }
    }
}
