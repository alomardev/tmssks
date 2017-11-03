package kfu.ccsit.tmssks.device_simulator.entities;

import java.util.ArrayList;
import kfu.ccsit.tmssks.device_simulator.api.Location;

public class Route {

    private String label;
    private ArrayList<Location> route;

    public Route(String label) {
        this.label = label;
        route = new ArrayList<>();
    }
    
    public void addLocation(double lat, double lng) {
        route.add(new Location(lat, lng));
    }
    
    public Location getLocation(int index) {
        return route.get(index);
    }
    
    public void clearLocations() {
        route.clear();
    }
    
    public int getSize() {
        return route.size();
    }

    @Override
    public String toString() {
        return label;
    }

}
