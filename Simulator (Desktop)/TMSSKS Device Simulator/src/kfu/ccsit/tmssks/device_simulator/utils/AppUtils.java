package kfu.ccsit.tmssks.device_simulator.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import kfu.ccsit.tmssks.device_simulator.Main;
import kfu.ccsit.tmssks.device_simulator.entities.Device;
import kfu.ccsit.tmssks.device_simulator.entities.Kid;
import kfu.ccsit.tmssks.device_simulator.entities.Route;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AppUtils {
    
    private final static String API_DOMAIN = Main.LOCAL ? "http://localhost/tmssks_web/api/" : "??";
    public final static String URL_LOAD_DATA = API_DOMAIN + "simulator-data.php";
    public final static String URL_SESSION = API_DOMAIN + "simulator-session.php";
    public final static String URL_READ_KID = API_DOMAIN + "simulator-read.php";
    public final static String URL_RECORD = API_DOMAIN + "simulator-record.php";
    
    public final static int STEPPER_FPS = 60;
    public final static int TYPE_TRANSPORTATION = 1;
    public final static int TYPE_SCHOOL = 2;

    private static ArrayList<Device> devicesList;
    private static ArrayList<Kid> kidsList;
    private static ArrayList<Route> routesList;

    public static ArrayList<Device> getDevices() {
        if (devicesList == null) {
            devicesList = new ArrayList<>();
        }
        return devicesList;
    }

    public static ArrayList<Kid> getKids() {
        if (kidsList == null) {
            kidsList = new ArrayList<>();
        }
        return kidsList;
    }

    public static ArrayList<Route> getRoutes() {
        if (routesList == null) {
            routesList = new ArrayList<>();
        }
        return routesList;
    }

    public static boolean parseJson(String jsonStr) {
        try {
            JSONObject obj = new JSONObject(jsonStr);
            JSONArray devices = obj.getJSONArray("devices");
            JSONArray kids = obj.getJSONArray("kids");

            getDevices().clear();
            for (int i = 0; i < devices.length(); i++) {
                JSONObject o = devices.getJSONObject(i);
                Device d = new Device(o.getInt("id"), o.getInt("type"), o.getString("holder"));
                getDevices().add(d);
            }

            getKids().clear();
            for (int i = 0; i < kids.length(); i++) {
                JSONObject o = kids.getJSONObject(i);
                Kid k = new Kid(o.getString("nid"), o.getString("name"));
                getKids().add(k);
            }
        } catch (JSONException ex) {
            return false;
        }
        return true;
    }

    public static void fetchRoutes() {
        getRoutes().clear();
        File directory = new File("routes");
        if (!directory.exists()) {
            directory.mkdir();
        }
        for (File f : directory.listFiles()) {
            String name = f.getName();
            int len = name.length();
            if (len > 6 && name.substring(len - 6).equals(".route")) {
                Route route = new Route(name.substring(0, len - 6));
                try (Scanner reader = new Scanner(f)) {
                    while (reader.hasNextDouble()) {
                        route.addLocation(reader.nextDouble(), reader.nextDouble());
                    }
                } catch (FileNotFoundException | InputMismatchException ex) {
                    System.err.println(ex.getMessage());
                }
                getRoutes().add(route);
            }
        }
    }

    public static String typeToString(int type) {
        return type == TYPE_TRANSPORTATION ? "Transportation" : "School";
    }

}
