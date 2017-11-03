package kfu.ccsit.tmssks.device_simulator.entities;

import kfu.ccsit.tmssks.device_simulator.api.Location;
import kfu.ccsit.tmssks.device_simulator.utils.AppUtils;

public class Device {
    
    public static final int TYPE_TRANSPORTATION = 1;
    public static final int TYPE_SCHOOL = 2;
    
    private int id;
    private int type;
    private String holder;
    
    private String log;
    
    private Route route;
    private int locationIndex;
    private boolean moving;
    private int seconds;
    private int frame;
    private int sessionId;
    
    private boolean on;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        locationIndex = 0;
        this.route = route;
    }
    
    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    private void setFrame(int frame) {
        this.frame = frame;
    }

    public int getLocationIndex() {
        return locationIndex;
    }

    private void setLocationIndex(int locationIndex) {
        this.locationIndex = locationIndex < 0 ? 0 :
                locationIndex > getRoute().getSize() - 1 ? getRoute().getSize() - 1 : locationIndex;
    }
    
    public boolean nextFrame() {
        int totalLocations = getRoute().getSize() - 1;
        double totalFrames = AppUtils.STEPPER_FPS * seconds;
        int bestIndex = (int) Math.round((++frame / totalFrames) * totalLocations);
        setLocationIndex(bestIndex);
        if (bestIndex == totalLocations) {
            setMoving(false);
            appendLog("Device reached the distination");
            return true;
        }
        return false;
    }
    
    public void resetRoute() {
        setFrame(0);
        setLocationIndex(0);
    }
    
    public void appendLog(String line) {
        if (!log.isEmpty()) {
            line += "\n------------------------------\n";
        }
        log = line + log;
    }
    
    public void clearLog() {
        log = "";
    }
    
    public Device(int id, int type, String holder) {
        this.id = id;
        this.type = type;
        this.holder = holder;
        this.seconds = 30;
        this.moving = false;
        this.locationIndex = 0;
        this.log = new String();
    }

    @Override
    public String toString() {
        return getHolder() + " (" + getId() + ")";
    }

    public Location getLocation() {
        return getRoute().getLocation(getLocationIndex());
    }
    
}
