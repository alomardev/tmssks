package kfu.ccsit.tmssks.data;

import com.google.android.gms.maps.model.LatLng;

public class MapPoint {

    private long time;
    private double lat, lng;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public MapPoint() {
        this(0, 0d, 0d);
    }

    public MapPoint(long time, double lat, double lng) {
        this.time = time;
        this.lat = lat;
        this.lng = lng;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }
}
