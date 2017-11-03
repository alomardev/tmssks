package kfu.ccsit.tmssks.device_simulator.api;

import kfu.ccsit.tmssks.device_simulator.utils.AppUtils;
import kfu.ccsit.tmssks.device_simulator.utils.RequestBuilder;

public class DeviceAPI {

    private final static long DELTA_READING_TIME = 1000 * 60 * 5; // Five minutes
    private final static double DELTA_DISTANCE = 3.5; // In meters

    private static Location lrl; // last recorded location

    public static void instantiateSession(int deviceId, RequestBuilder.OnRequestListener callback) {
        RequestBuilder.instance(AppUtils.URL_SESSION).callback(callback).method("POST").put("device_id", deviceId).request();
    }

    public static void uploadRecord(int sessionId, Location location, RequestBuilder.OnRequestListener callback) {
        if (lrl != null) {
            double dist = MapUtils.distance(lrl.getLatitude(),
                    lrl.getLongitude(), location.getLatitude(), location.getLongitude());
            if (Math.abs(dist) <= DELTA_DISTANCE) {
                return;
            }
        }
        RequestBuilder.instance(AppUtils.URL_RECORD).callback(callback).method("POST")
                .put("session_id", sessionId)
                .put("latitude", location.getLatitude())
                .put("longitude", location.getLongitude())
                .request();
        lrl = location;
    }

    public static void readKid(String nid, int sessionId, int deviceId, RequestBuilder.OnRequestListener callback) {
        RequestBuilder.instance(AppUtils.URL_READ_KID)
                .callback(callback).method("POST").put("kid_ids", nid)
                .put("session_id", sessionId)
                .put("device_id", deviceId)
                .request();
    }
}
