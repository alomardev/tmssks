package kfu.ccsit.tmssks;

import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {

    public static long getSystemTimeForServer() {
        return System.currentTimeMillis() / 1000;
    }

    public static long toServerTime(long millis) {
        return (millis - TimeZone.getDefault().getRawOffset()) / 1000;
    }

    public static long toDeviceTime(long unixTime) {
        return (unixTime * 1000) + TimeZone.getDefault().getRawOffset();
    }

    public static String formatTime(long millis) {
        long seconds = millis / 1000;
        long m = (seconds / 60) % 60;
        long h = (seconds / 3600) % 24;
        return String.format(Locale.US, "%02d:%02d", h, m);
    }

    public static String getFormattedTime(int hours, int minutes) {
        return String.format(Locale.US, "%02d%02d", hours, minutes);
    }

    public static int getFormattedTimeHour(String formattedTime) {
        formattedTime = formattedTime.replace(":", "");
        while (formattedTime.length() < 4)
            formattedTime = "0" + formattedTime;
        return Integer.parseInt(formattedTime.substring(0, 2));
    }

    public static int getFormattedTimeMinute(String formattedTime) {
        formattedTime = formattedTime.replace(":", "");
        while (formattedTime.length() < 4)
            formattedTime = "0" + formattedTime;

        return Integer.parseInt(formattedTime.substring(2, 4));
    }

    public static String getReadableTime(String formattedTime) {
        int h = getFormattedTimeHour(formattedTime);
        int m = getFormattedTimeMinute(formattedTime);
        boolean isAM = h < 12;

        if (h > 12)
            h = h - 12;
        else if (h == 0)
            h = 12;

        return String.format(Locale.US, "%d:%02d %s", h, m, isAM ? "AM" : "PM");
    }

}
