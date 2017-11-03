package kfu.ccsit.tmssks.data;

import java.util.ArrayList;
import java.util.List;

public class Kid {

    private double lat, lng;

    private String id;
    private String name;
    private String level;
    private String address;

    private School school;
    private Trans trans;

    private List<TimelineRecord> records;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Trans getTrans() {
        return trans;
    }

    public void setTrans(Trans trans) {
        this.trans = trans;
    }

    public List<TimelineRecord> getRecords() {
        return records;
    }

    public Kid() {
        records = new ArrayList<>();
        this.lat = 25.348369861;
        this.lng = 49.636230469;
    }
}
