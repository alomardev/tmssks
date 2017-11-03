package kfu.ccsit.tmssks.device_simulator;

import java.io.File;
import java.util.ArrayList;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import kfu.ccsit.tmssks.device_simulator.api.DeviceAPI;
import kfu.ccsit.tmssks.device_simulator.entities.Device;
import kfu.ccsit.tmssks.device_simulator.entities.Kid;
import kfu.ccsit.tmssks.device_simulator.entities.Route;
import kfu.ccsit.tmssks.device_simulator.utils.AppUtils;
import kfu.ccsit.tmssks.device_simulator.utils.RequestBuilder;
import kfu.ccsit.tmssks.device_simulator.utils.UIUtils;

public class Main implements MainFrame.MainFrameListener, KidsListDialog.KidsListDialogListener {
    
    public static final boolean LOCAL = true;

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            System.err.println(ex.getMessage());
        }

        Main main = new Main();
        main.start();

        System.out.println(new File("test.in").getAbsolutePath());
    }

    private MainFrame mainFrm;
    private KidsListDialog kidsListDialog;
    private Device currentDevice;
    private Stepper stepper;
    private AboutDialog aboutDialog;

    public Main() {
        mainFrm = new MainFrame(AppUtils.getDevices(), AppUtils.getRoutes());
        kidsListDialog = new KidsListDialog(AppUtils.getKids(), mainFrm);
    }

    public void start() {
        mainFrm.setup();
        
        UIUtils.center(mainFrm);
        
        mainFrm.setVisible(true);
        mainFrm.setMainFrameListener(this);

        kidsListDialog.setup();
        kidsListDialog.setKidsListDialogListener(this);
        sync();
        
        stepper = new Stepper(AppUtils.STEPPER_FPS);
        stepper.setOnEnterFrameListener((frame) -> {
            AppUtils.getDevices().forEach((device) -> {
                if (device.isOn() && device.isMoving()) {
                    if (device.nextFrame()) {
                        mainFrm.updateDeviceLog();
                    }
                    mainFrm.refreshRouteSettings();
                    DeviceAPI.uploadRecord(device.getSessionId(), device.getLocation(), (id, response, error) -> {
                        // System.out.println(response);
                    });
                }
            });
        });
        stepper.play();
        
        aboutDialog = new AboutDialog(mainFrm);
        aboutDialog.setup();
    }

    public void sync() {
        mainFrm.updateStatus("Loading data...");
        AppUtils.getDevices().clear();
        AppUtils.getKids().clear();
        AppUtils.getRoutes().clear();
        mainFrm.refresh();
        RequestBuilder.instance(AppUtils.URL_LOAD_DATA).callback((id, response, error) -> {
            if (!error) {
                if (!AppUtils.parseJson(response)) {
                    mainFrm.updateStatus("Couldn't load data!");
                    return;
                }
                AppUtils.fetchRoutes();
                
                mainFrm.refresh();
                
                mainFrm.updateStatus("Data loaded successfully", 3);
            } else
                mainFrm.updateStatus("Error while loading data!", 3);
        }).request();
        System.out.println(AppUtils.URL_LOAD_DATA);
    }

    @Override
    public void onDeviceSelected(Device device) {
        currentDevice = device;
        mainFrm.refreshDeviceProperties();
        mainFrm.setDeviceRunning(device.isOn());
    }

    @Override
    public void onRouteSelected(Route route) {
        if (currentDevice != null) {
            currentDevice.setRoute(route);
        }
        mainFrm.refreshRouteSettings();
    }

    @Override
    public void onButtonClick(int button) {
        switch (button) {
            case MainFrame.BUTTON_ADD_DEVICE:
                System.out.println("Button clicked: BUTTON_ADD_DEVICE");
                break;
            case MainFrame.BUTTON_REMOVE_DEVICE:
                System.out.println("Button clicked: BUTTON_REMOVE_DEVICE");
                break;
            case MainFrame.BUTTON_EDIT_DEVICE:
                System.out.println("Button clicked: BUTTON_EDIT_DEVICE");
                break;
            case MainFrame.BUTTON_SYNC:
                if (UIUtils.confirm(mainFrm, "Reloading data will reset the simulator.\nDo you want to continue?"))
                    sync();
                break;
            case MainFrame.BUTTON_READ_TAG:
                
                UIUtils.center(kidsListDialog, mainFrm);
                kidsListDialog.refresh();
                kidsListDialog.setVisible(true);
                
                break;
            case MainFrame.BUTTON_ROUTE_PLAY:
                if (currentDevice != null) {
                    currentDevice.setMoving(true);
                    mainFrm.refreshRouteSettings();
                    currentDevice.appendLog("Device is moving");
                    mainFrm.updateDeviceLog();
                }
                
                break;
            case MainFrame.BUTTON_ROUTE_PAUSE:
                if (currentDevice != null) {
                    currentDevice.setMoving(false);
                    mainFrm.refreshRouteSettings();
                    currentDevice.appendLog("Device paused at\n" + currentDevice.getLocation());
                    mainFrm.updateDeviceLog();
                }
                
                break;
            case MainFrame.BUTTON_ROUTE_STOP:
                if (currentDevice != null) {
                    currentDevice.setMoving(false);
                    currentDevice.resetRoute();
                    mainFrm.refreshRouteSettings();
                    currentDevice.appendLog("Device stopped");
                    mainFrm.updateDeviceLog();
                }
                
                break;
            case MainFrame.BUTTON_INFO:
                UIUtils.center(aboutDialog, mainFrm);
                aboutDialog.setVisible(true);
                break;
            case MainFrame.BUTTON_DEVICE_POWER:
                currentDevice.setOn(!currentDevice.isOn());
                currentDevice.appendLog("Device turned " + (currentDevice.isOn() ? "ON" : "OFF"));
                mainFrm.refreshDeviceProperties();
                if (currentDevice.isOn()) {
                    DeviceAPI.instantiateSession(currentDevice.getId(), (id, response, error) -> {
                        if (!error) {
                            currentDevice.setSessionId(Integer.parseInt(response));
                            currentDevice.appendLog("Session in use: " + response);
                            mainFrm.updateDeviceLog();
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onRoutingTimeChanged(int seconds) {
        currentDevice.setSeconds(seconds);
    }

    @Override
    public void onSubmitKids(ArrayList<Kid> kids) {
        mainFrm.updateStatus("Reading tag(s)...");
        String nids = "";
        for (Kid k : kids) {
            if (!nids.isEmpty()) {
                nids += "|";
            }
            nids += k.getNationalId();
        }
        DeviceAPI.readKid(nids, currentDevice.getSessionId(), currentDevice.getId(), (id, response, error) -> {
            if (error || response.equals("error")) {
                System.out.println("readKid: " + response);
                mainFrm.updateStatus("Cound't read the tag(s)!");
            } else {
                currentDevice.appendLog(response);
                mainFrm.updateDeviceLog();
                mainFrm.updateStatus(response, 3);
            }
        });
    }
}
