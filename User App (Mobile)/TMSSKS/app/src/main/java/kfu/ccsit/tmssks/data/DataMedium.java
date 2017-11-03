package kfu.ccsit.tmssks.data;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat.InboxStyle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import kfu.ccsit.tmssks.ActivityAlert;
import kfu.ccsit.tmssks.ActivityLogin;
import kfu.ccsit.tmssks.ActivityMain;
import kfu.ccsit.tmssks.LogUtils;
import kfu.ccsit.tmssks.PrefUtils;
import kfu.ccsit.tmssks.R;
import kfu.ccsit.tmssks.TimeUtils;
import kfu.ccsit.tmssks.data.network.NetworkUtils;
import kfu.ccsit.tmssks.services.NotifierService;

public class DataMedium {

    public static final int TYPE_TRANS = 1;
    public static final int TYPE_SCHOOL = 2;

    private static User sUser;
    private static List<Kid> sKids;
    private static List<School> sSchools;
    private static List<Trans> sTrans;

    public static boolean login(Context context) {
        SharedPreferences defaultSP = PrefUtils.getDefault(context);
        int id = defaultSP.getInt(PrefUtils.KEY_USER_ID, -1);
        if (id > -1) {
            DataMedium.setUser(id, defaultSP.getString(PrefUtils.KEY_USER_USERNAME, ""),
                    defaultSP.getString(PrefUtils.KEY_USER_NAME, ""),
                    defaultSP.getString(PrefUtils.KEY_USER_EMAIL, ""));

            context.startService(new Intent(context, NotifierService.class));
            return true;
        }
        return false;
    }

    public static void logout(Context context) {
        if (context instanceof Activity) {
            context.startActivity(new Intent(context, ActivityLogin.class));
            ((Activity) context).finish();
        }
        PrefUtils.getDefault(context).edit()
                .remove(PrefUtils.KEY_USER_ID)
                .remove(PrefUtils.KEY_USER_USERNAME)
                .remove(PrefUtils.KEY_USER_NAME)
                .remove(PrefUtils.KEY_USER_EMAIL)
                .apply();
        sUser = null;
        context.stopService(new Intent(context, NotifierService.class));
    }

    private static void setUser(int id, String username, String name, String email) {
        sUser = new User(id, name, username, email, 2);
    }

    public static User getUser() {
        if (sUser == null) {
            sUser = new User(-1, "", "", "", -1);
        }
        return sUser;
    }

    public static List<Kid> getKids() {
        if (sKids == null) {
            sKids = new ArrayList<>();
        }
        return sKids;
    }

    public static Kid getKid(String nid) {
        for (Kid k : getKids()) {
            if (k.getId().equals(nid)) {
                return k;
            }
        }
        return null;
    }

    public static List<School> getSchools() {
        if (sSchools == null) {
            sSchools = new ArrayList<>();
        }
        return sSchools;
    }

    public static School getSchool(int id) {
        for (School s : sSchools) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public static List<Trans> getTrans() {
        if (sTrans == null) {
            sTrans = new ArrayList<>();
        }
        return sTrans;
    }

    public static Trans getTrans(int id) {
        for (Trans t : sTrans) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public static void authorize(final Context context, String username, String password,
                                 final NetworkUtils.OnRequestListener listener) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        NetworkUtils.OnRequestListener callback = new NetworkUtils.OnRequestListener() {
            @Override
            public void onFinished(String response, boolean error) {
                if (error) {
                    if (listener != null) {
                        listener.onFinished(response, true);
                    }
                    return;
                }
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("authorized")) {
                        JSONObject user = obj.getJSONObject("data");

                        PrefUtils.getDefault(context).edit()
                                .putInt(PrefUtils.KEY_USER_ID, user.getInt("id"))
                                .putString(PrefUtils.KEY_USER_USERNAME, user.getString("username"))
                                .putString(PrefUtils.KEY_USER_NAME, user.getString("name"))
                                .putString(PrefUtils.KEY_USER_EMAIL, user.getString("email"))
                                .apply();

                        if (listener != null) {
                            listener.onFinished(response, false);
                        }
                    } else {
                        if (listener != null) {
                            listener.onFinished("Invalid username and password", true);
                        }
                    }
                } catch (JSONException e) {
                    LogUtils.d(response);
                    e.printStackTrace();
                }
            }
        };

        NetworkUtils.request(context, NetworkUtils.API_AUTHORIZE_USER_URL, params, callback);
    }

    public static void loadStaticData(final Context context,
                                      final NetworkUtils.OnRequestListener listener) {
        Map<String, String> params = new HashMap<>();
        if (getUser().getId() > -1) {
            params.put("user_id", Integer.toString(getUser().getId()));
        }

        NetworkUtils.OnRequestListener callback = new NetworkUtils.OnRequestListener() {
            @Override
            public void onFinished(String response, boolean error) {
                if (error) {
                    if (listener != null) {
                        listener.onFinished(response, true);
                    }
                    return;
                }
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray kidsArray = root.getJSONArray("kids");
                    JSONArray schoolsArray = root.getJSONArray("schools");
                    JSONArray transArray = root.getJSONArray("trans");

                    // Loading trans
                    getTrans().clear();
                    for (int i = 0; i < transArray.length(); i++) {
                        JSONObject obj = transArray.getJSONObject(i);
                        Trans t = new Trans();
                        t.setId(obj.getInt("id"));
                        t.setNumPlate(obj.getString("num_plate"));
                        t.setDriverName(obj.getString("driver_name"));
                        t.setDriverPhone(obj.getString("driver_phone"));
                        getTrans().add(t);
                    }

                    // Loading schools
                    getSchools().clear();
                    for (int i = 0; i < schoolsArray.length(); i++) {
                        JSONObject obj = schoolsArray.getJSONObject(i);
                        School s = new School(obj.getInt("id"), obj.getString("name"));
                        getSchools().add(s);
                    }

                    // Loading kids
                    getKids().clear();
                    for (int i = 0; i < kidsArray.length(); i++) {
                        JSONObject obj = kidsArray.getJSONObject(i);
                        Kid k = new Kid();
                        k.setId(obj.getString("nid"));
                        k.setName(obj.getString("name"));
                        k.setLevel(obj.getString("level"));
                        if (!obj.isNull("address_title") && !obj.isNull("address_latitude") && obj.isNull("address_longitude")) {
                            k.setAddress(obj.getString("address_title"));
                            k.setLat(obj.getDouble("address_latitude"));
                            k.setLng(obj.getDouble("address_longitude"));
                        }
                        if (!obj.isNull("trans_id")) {
                            k.setTrans(DataMedium.getTrans(obj.getInt("trans_id")));
                        }
                        if (!obj.isNull("school_id")) {
                            k.setSchool(DataMedium.getSchool(obj.getInt("school_id")));
                        }
                        getKids().add(k);
                    }

                    if (listener != null)
                        listener.onFinished(response, false);

                } catch (JSONException ex) {
                    if (listener != null)
                        listener.onFinished("JSON Exception: " + ex.getMessage(), true);
                }
            }
        };

        NetworkUtils.request(context, NetworkUtils.API_GET_STATIC_DATA, params, callback);
    }

    public static void loadTimelineRecords(final Context context,
                                           final NetworkUtils.OnRequestListener listener) {
        Map<String, String> params = new HashMap<>();
        if (getUser().getId() > -1) {
            params.put("user_id", Integer.toString(getUser().getId()));
        }

        NetworkUtils.OnRequestListener callback = new NetworkUtils.OnRequestListener() {
            @Override
            public void onFinished(String response, boolean error) {
                if (error) {
                    if (listener != null)
                        listener.onFinished(response, true);
                    return;
                }

                try {
                    JSONObject obj = new JSONObject(response);
                    for (Kid k : DataMedium.getKids()) {
                        k.getRecords().clear();
                        if (!obj.has(k.getId())) {
                            continue;
                        }
                        JSONArray arr = obj.getJSONArray(k.getId());
                        int size = arr.length();
                        for (int i = 0; i < size; i++) {
                            JSONObject jsonRecord = arr.getJSONObject(i);
                            TimelineRecord record = new TimelineRecord();
                            record.setTime(TimeUtils.toDeviceTime(jsonRecord.getLong("time")));
                            record.setEntered(jsonRecord.getBoolean("entered"));
                            record.setHolderName(jsonRecord.getString("holder_name"));
                            record.setReadingId(jsonRecord.getInt("reading_id"));
                            record.setType(jsonRecord.getInt("type"));
                            k.getRecords().add(record);
                        }
                    }
                    if (listener != null)
                        listener.onFinished(response, false);
                } catch (JSONException ex) {
                    if (listener != null)
                        listener.onFinished("JSONException: " + ex.getMessage(), true);
                }
            }
        };

        NetworkUtils.request(context, NetworkUtils.API_GET_TIMELINE_RECORDS, params, callback);
    }

    public static void loadNotifications(final Context context,
                                         final NetworkUtils.OnRequestListener listener) {
        final boolean enableNotification =
                PrefUtils.getDefault(context).getBoolean(context.getString(R.string.pref_key_master_enable), true);
        final boolean enableAlert = enableNotification &&
                PrefUtils.getDefault(context).getBoolean(context.getString(R.string.pref_key_alert_enable), true);
        if (!enableNotification) {
            return;
        }
        boolean seconds = PrefUtils.getDefault(context).getBoolean("use_seconds", false);
        final long timeout = PrefUtils.getDefault(context).getInt(context.getString(R.string.pref_key_alert_time), 30) * (seconds ? 1 : 60);
        final String doneAlerts = PrefUtils.getDefault(context).getString(PrefUtils.KEY_DONE_ALERTS, "");
        final String doneNotify = PrefUtils.getDefault(context).getString(PrefUtils.KEY_DONE_NOTIFY, "");

        Map<String, String> params = new HashMap<>();
        params.put("user_id", Integer.toString(getUser().getId()));
        params.put("alert_timeout", Long.toString(timeout));
        params.put("done_notify", doneNotify);
        params.put("done_alerts", doneAlerts);

        NetworkUtils.OnRequestListener callback = new NetworkUtils.OnRequestListener() {
            @Override
            public void onFinished(String response, boolean error) {
                if (error) {
                    if (listener != null)
                        listener.onFinished(response, false);
                    return;
                }

                String phoneschool = "";
                String phonedriver = "";
                try {
                    LogUtils.d(response);
                    String alertIds = doneAlerts;
                    String notifIds = doneNotify;
                    JSONArray arr = new JSONArray(response);
                    List<String> messages1 = new ArrayList<>();
                    List<String> messages2 = new ArrayList<>();

                    int notId = 0;
                    int length = arr.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject not = arr.getJSONObject(i);
                        JSONObject record = not.getJSONObject("record");
                        String nid = not.getString("nid");
                        int type = not.getInt("type");
                        int readingId = record.getInt("reading_id");
                        notId = readingId;
                        // ---
                        Kid k = getKid(nid);
                        long time = TimeUtils.toDeviceTime(record.getLong("time"));
                        int deviceType = record.getInt("type");
                        String holderName = record.getString("holder_name");
                        boolean entered = record.getBoolean("entered");
                        if (k != null) {
                            if (type == 1) {
                                String m = k.getName();
                                m += " has entered the bus (";
                                m += holderName;
                                m += ") since ";
                                m += TimeUtils.formatTime(time);
                                m += " and did not leave";
                                messages1.add(m);
                                phonedriver = k.getTrans().getDriverPhone();
                            }
                            if (type == 2) {
                                String m = String.format(Locale.getDefault(),
                                        "%s has %s the %s (%s) at %s",
                                        k.getName(),
                                        entered ? "entered" : "left",
                                        deviceType == TYPE_SCHOOL ? "school" : "bus",
                                        holderName,
                                        TimeUtils.formatTime(time));
                                messages2.add(m);
                            }
                        }

                        // ---

                        if (type == 1) {
                            if (!alertIds.isEmpty()) {
                                alertIds += "|";
                            }
                            alertIds += readingId;
                        } else if (type == 2) {
                            if (!notifIds.isEmpty()) {
                                notifIds += "|";
                            }
                            notifIds += readingId;
                        }
                    }

                    PrefUtils.getDefault(context).edit()
                            .putString(PrefUtils.KEY_DONE_ALERTS, alertIds)
                            .putString(PrefUtils.KEY_DONE_NOTIFY, notifIds)
                            .apply();

                    if (messages2.size() > 0) {
                        Intent intent = new Intent(context, ActivityMain.class);
                        PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                                0);

                        NotificationCompat.Builder b = new NotificationCompat.Builder(context);
                        b.setContentTitle("Monitoring System")
                                .setSmallIcon(R.drawable.ic_not)
                                .setDefaults(Notification.DEFAULT_SOUND)
                                .setContentIntent(pi)
                                .setAutoCancel(true)
                                .setContentText("Kids have taken actions")
                                .setColor(ContextCompat.getColor(context, R.color.colorAccent));

                        if (messages2.size() == 1) {
                            b.setContentText(messages2.get(0));
                        } else {
                            InboxStyle style = new InboxStyle();
                            for (String m : messages2) {
                                style.addLine(m);
                            }
                            style.setSummaryText(messages2.size() + " actions");
                            b.setStyle(style);
                        }

                        NotificationManagerCompat nm = NotificationManagerCompat.from(context);
                        nm.notify(notId, b.build());

                    }

                    if (messages1.size() > 0 && enableAlert) {
                        String message = "";
                        boolean first = true;
                        for (String m : messages1) {
                            if (!first) {
                                message += "\n";
                            }
                            message += m;
                            first = false;
                        }

                        Intent intent = new Intent(context, ActivityAlert.class);
                        intent.putExtra(ActivityAlert.KEY_MESSAGE_EXTRA, message);
                        intent.putExtra(ActivityAlert.KEY_PHONE_DRIVER_EXTRA, phonedriver);
                        context.startActivity(intent);
                    }

                    if (listener != null) {
                        listener.onFinished(response, false);
                    }

                } catch (JSONException ex) {
                    LogUtils.d(response);
                    if (listener != null)
                        listener.onFinished("JSONException: " + ex.getMessage(), false);
                }
            }
        };

        NetworkUtils.request(context, NetworkUtils.API_GET_NOTIFICATIONS, params, callback);
    }

    public static void loadRoute(final Context context,
                                 final NetworkUtils.OnRequestListener listener,
                                 final String nid) {
        LogUtils.d("loadRoute: " + nid);
        Map<String, String> params = new HashMap<>();
        params.put("kid_id", nid);

        NetworkUtils.request(context, NetworkUtils.API_GET_ROUTE, params, listener);
    }

    public static MapPoint parseMapPoints(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return new MapPoint(
                    TimeUtils.toDeviceTime(obj.getLong("time")),
                    obj.getDouble("latitude"),
                    obj.getDouble("longitude")
            );
        } catch (JSONException ex) {
            LogUtils.d("JSONException: " + ex.getMessage());
            return null;
        }
    }
}
