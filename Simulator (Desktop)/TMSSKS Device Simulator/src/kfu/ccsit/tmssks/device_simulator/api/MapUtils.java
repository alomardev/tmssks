package kfu.ccsit.tmssks.device_simulator.api;

public class MapUtils {

    public static double distance(double lat1, double lng1, double lat2, double lng2) {
        double R = 6378.137;
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLng = lng2 * Math.PI / 180 - lng1 * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180)
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d * 1000;
    }

    public static Location middle(double lat1, double lng1, double lat2, double lng2) {
        double dLng = Math.toRadians(lng2 - lng1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lng1 = Math.toRadians(lng1);
        double Bx = Math.cos(lat2) * Math.cos(dLng);
        double By = Math.cos(lat2) * Math.sin(dLng);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2),
                Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lng3 = lng1 + Math.atan2(By, Math.cos(lat1) + Bx);

        return new Location(Math.toDegrees(lat3), Math.toDegrees(lng3));
    }
}
