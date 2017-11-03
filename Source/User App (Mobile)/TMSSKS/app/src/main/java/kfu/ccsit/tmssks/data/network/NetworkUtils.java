package kfu.ccsit.tmssks.data.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import kfu.ccsit.tmssks.PrefUtils;

public class NetworkUtils {

    public interface OnRequestListener {

        void onFinished(String response, boolean error);
    }

    public static final String API_AUTHORIZE_USER_URL = "app-authorize-user.php";
    public static final String API_GET_STATIC_DATA = "app-get-static-data.php";
    public static final String API_GET_TIMELINE_RECORDS = "app-get-timeline-records.php";
    public static final String API_GET_NOTIFICATIONS = "app-get-notifications.php";
    public static final String API_GET_ROUTE = "app-get-route.php";

    private static RequestQueue sRequestQueue;

    private static RequestQueue getRequestQueue(Context context) {
        if (sRequestQueue == null) {
            sRequestQueue = Volley.newRequestQueue(context);
        }
        return sRequestQueue;
    }

    public static void request(Context context, String url,
                               Map<String, String> params,
                               final OnRequestListener callback) {
        String domain = "http://" + PrefUtils.getDefault(context).getString("localhost_ip", "192.168.1.8") + "/tmssks/api/";
        RequestSimpleString simpleRequest = new RequestSimpleString(domain + url, params,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onFinished(response, false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onFinished("Error: " + error.getMessage(), true);
                    }
                });
        getRequestQueue(context).add(simpleRequest);
    }

}
