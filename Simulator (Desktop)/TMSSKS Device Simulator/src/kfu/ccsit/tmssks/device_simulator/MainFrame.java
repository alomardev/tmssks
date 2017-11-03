package kfu.ccsit.tmssks.device_simulator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import kfu.ccsit.tmssks.device_simulator.components.IconButton;
import kfu.ccsit.tmssks.device_simulator.components.SeekBar;
import kfu.ccsit.tmssks.device_simulator.entities.Device;
import kfu.ccsit.tmssks.device_simulator.entities.Route;
import kfu.ccsit.tmssks.device_simulator.utils.AppUtils;
import kfu.ccsit.tmssks.device_simulator.utils.UIUtils;

public class MainFrame extends JFrame implements DeviceBox.DeviceBoxListener, ActionListener, DocumentListener,
        ItemListener {

    private final static String DEFAULT_STATUS = "...";

    public final static int BUTTON_ADD_DEVICE = 1;
    public final static int BUTTON_REMOVE_DEVICE = 2;
    public final static int BUTTON_EDIT_DEVICE = 3;
    public final static int BUTTON_SYNC = 4;
    public final static int BUTTON_READ_TAG = 5;
    public final static int BUTTON_ROUTE_PLAY = 6;
    public final static int BUTTON_ROUTE_PAUSE = 7;
    public final static int BUTTON_ROUTE_STOP = 8;
    public final static int BUTTON_INFO = 9;
    public final static int BUTTON_DEVICE_POWER = 10;

    public static interface MainFrameListener {

        void onDeviceSelected(Device device);

        void onRouteSelected(Route route);

        void onButtonClick(int button);

        void onRoutingTimeChanged(int seconds);
    }

    private JPanel deviceActionsPanel;
    private JPanel devicePanel;
    private IconButton addDeviceBtn;
    private IconButton deleteDeviceBtn;
    private IconButton editDeviceBtn;
    private IconButton syncBtn;
    private JComboBox devicesCombo;
    private JLabel statusLbl;
    private IconButton infoBtn;
    private DeviceBox deviceBox;
    private JButton readTagBtn;
    private JComboBox routesCombo;
    private JTextField timeField;
    private IconButton playBtn;
    private IconButton pauseBtn;
    private IconButton stopBtn;
    private JTextArea deviceLogArea;
    private JScrollPane deviceLogSP;
    private JLabel deviceIdLbl;
    private JLabel typeLbl;
    private JLabel holderLbl;
    private SeekBar seekbar;
    private JLabel routeLbl;
    private JLabel timeLbl;

    private Timer timer;
    private ArrayList<Device> devices;
    private ArrayList<Route> routes;
    private MainFrameListener callback;

    public void setMainFrameListener(MainFrameListener callback) {
        this.callback = callback;
    }

    public MainFrame(ArrayList<Device> devices, ArrayList<Route> routes) {
        this.devices = devices;
        this.routes = routes;
        timer = new Timer();
    }

    public void refresh() {
        routesCombo.removeAllItems();
        if (routes != null) {
            routes.forEach((r) -> {
                routesCombo.addItem(r);
            });
        }
        devicesCombo.removeAllItems();
        if (devices != null) {
            devices.forEach((d) -> {
                devicesCombo.addItem(d);
            });
        }

        refreshDeviceProperties();
    }

    public void refreshDeviceProperties() {
        if (devicesCombo.getSelectedItem() == null || !(devicesCombo.getSelectedItem() instanceof Device)) {
            enableInside(devicePanel, false);
            return;
        }
        enableInside(devicePanel, true);

        Device device = ((Device) devicesCombo.getSelectedItem());
        enableInside(deviceActionsPanel, device.isOn());

        deviceIdLbl.setText("Device ID: " + device.getId());
        typeLbl.setText("Type: " + AppUtils.typeToString(device.getType()));
        holderLbl.setText("Holder: " + device.getHolder());
        routesCombo.setSelectedItem(null);

        if (device.getType() == AppUtils.TYPE_TRANSPORTATION) {
            routesCombo.setSelectedItem(device.getRoute());

            timeField.getDocument().removeDocumentListener(this);
            timeField.setText("" + device.getSeconds());
            timeField.getDocument().addDocumentListener(this);
        } else {
            timeField.getDocument().removeDocumentListener(this);
            timeField.setText("");
            timeField.getDocument().addDocumentListener(this);
        }
        refreshRouteSettings();
        updateDeviceLog();

    }

    public void refreshRouteSettings() {
        if (devicesCombo.getSelectedItem() == null || !(devicesCombo.getSelectedItem() instanceof Device)) {
            return;
        }
        Device device = ((Device) devicesCombo.getSelectedItem());

        if (device.getType() != AppUtils.TYPE_TRANSPORTATION) {
            routesCombo.setEnabled(false);
            routeLbl.setEnabled(false);
        }

        if (device.getRoute() != null && device.isOn()) {
            seekbar.setEnabled(true);
            playBtn.setEnabled(!device.isMoving() && device.getLocationIndex() < device.getRoute().getSize() - 1);
            pauseBtn.setEnabled(device.isMoving() && device.getLocationIndex() < device.getRoute().getSize() - 1);
            stopBtn.setEnabled(device.getLocationIndex() > 0);

            routesCombo.setEnabled(!device.isMoving() && device.getLocationIndex() == 0);
            timeField.setEnabled(routesCombo.isEnabled());
            timeLbl.setEnabled(timeField.isEnabled());

            seekbar.setMax(device.getRoute().getSize() - 1);
            updateSeekBarPosition(device.getLocationIndex());
        } else {
            playBtn.setEnabled(false);
            seekbar.setEnabled(false);
            pauseBtn.setEnabled(false);
            stopBtn.setEnabled(false);
            timeField.setEnabled(false);
            timeLbl.setEnabled(false);

            seekbar.setMax(0);
            updateSeekBarPosition(0);
        }
    }

    public void updateSeekBarPosition(int position) {
        seekbar.setCurrent(position);
    }

    public void updateDeviceLog() {
        if (devicesCombo.getSelectedItem() == null || !(devicesCombo.getSelectedItem() instanceof Device)) {
            return;
        }

        Device device = ((Device) devicesCombo.getSelectedItem());
        deviceLogArea.setText(device.getLog());
        SwingUtilities.invokeLater(() -> {
            deviceLogSP.getVerticalScrollBar().setValue(0);
        });
    }

    public void setDeviceRunning(boolean running) {
        deviceBox.setRunning(running);
    }

    public void updateStatus(String status, int seconds) {
        statusLbl.setText(status);
        if (seconds > 0) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    statusLbl.setText(DEFAULT_STATUS);
                }
            }, seconds * 1000);
        }
    }

    public void updateStatus(String status) {
        updateStatus(status, 0);
    }

    @Override
    public void onPowerButtonClick(boolean isOn) {
        if (callback != null) {
            callback.onButtonClick(BUTTON_DEVICE_POWER);
        }
    }

    public void setup() {
        setTitle("Hardware Simulation");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /* COMPONENTS ----------------------------------------------- */
        JPanel mainMenuPanel = new JPanel(new GridBagLayout());
        devicePanel = new JPanel(new GridBagLayout());
        JPanel logPanel = new JPanel(new GridBagLayout());
        mainMenuPanel.setEnabled(false);

        // Main Menu Components
        JLabel headerLbl = new JLabel("TMSSKS Tracker Simulator");
        addDeviceBtn = new IconButton("ic_add.png");
        deleteDeviceBtn = new IconButton("ic_delete.png");
        editDeviceBtn = new IconButton("ic_edit.png");
        syncBtn = new IconButton("ic_sync.png", 8, true);
        JLabel devicesLbl = new JLabel("Devices:");
        devicesCombo = new JComboBox(new Object[]{"Select a device..."});
        devicesCombo.setPreferredSize(new Dimension(200, devicesCombo.getPreferredSize().height));

        // Log Components
        statusLbl = new JLabel("Status!");
        infoBtn = new IconButton("ic_info.png", 8, true);

        // Device Panel Components
        deviceBox = new DeviceBox();

        JPanel deviceInfoPanel = new JPanel(new GridBagLayout());
        deviceIdLbl = new JLabel("ID...");
        typeLbl = new JLabel("Type...");
        holderLbl = new JLabel("Holder...");

        deviceActionsPanel = new JPanel(new GridBagLayout());
        JLabel readTagLbl = new JLabel("Read tags:");
        readTagBtn = new JButton("Select Kids");
        routesCombo = new JComboBox(new Object[]{"Select a route..."});
        routeLbl = new JLabel("Route:");
        timeLbl = new JLabel("Seconds:");
        timeField = new JTextField(1);
        playBtn = new IconButton("ic_play.png", 2, true);
        pauseBtn = new IconButton("ic_pause.png", 2, true);
        stopBtn = new IconButton("ic_stop.png", 2, true);
        seekbar = new SeekBar();

        JPanel deviceLogPanel = new JPanel(new GridBagLayout());
        deviceLogArea = new JTextArea(5, 30);
        deviceLogSP = new JScrollPane(deviceLogArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        /* lAYOUTING ------------------------------------------------ */
        GridBagConstraints c = new GridBagConstraints();

        // Main Menu
        UIUtils.gbc(c, 0, 0, 1, 2, 1, 0, -1, true, false, 2, 1, 8, 1);
        mainMenuPanel.add(headerLbl, c);
        UIUtils.gbc(c, 1, 0, 1, 1, 0, 0, -1, true, false, 0, 1, 1, 0);
        mainMenuPanel.add(devicesLbl, c);
        UIUtils.gbc(c, 1, 1, 1, 1, 0, 0, -1, false, false, 0, 1, 1, 2);
        mainMenuPanel.add(devicesCombo, c);
        // UIUtils.gbc(c, 2, 0, 1, 1, 0, 0, -1, false, false, 0, 1, 1, 1);
        // mainMenuPanel.add(addDeviceBtn, c);
        // UIUtils.gbc(c, 3, 0, 1, 1, 0, 0, -1, false, false, 0, 1, 1, 1);
        // mainMenuPanel.add(deleteDeviceBtn, c);
        // UIUtils.gbc(c, 4, 0, 1, 1, 0, 0, -1, false, false, 0, 1, 1, 1);
        // mainMenuPanel.add(editDeviceBtn, c);
        // UIUtils.gbc(c, 5, 0, 1, 2, 0, 0, -1, false, true, 1, 1, 1, 1);
        // mainMenuPanel.add(new JSeparator(JSeparator.VERTICAL), c);
        UIUtils.gbc(c, 6, 0, 1, 2, 0, 0, -1, false, false, 1, 2, 2, 2);
        mainMenuPanel.add(syncBtn, c);
        UIUtils.gbc(c, 0, 2, 100, 2, 0, 0, -1, true, false, 0, 0, 0, 0);
        mainMenuPanel.add(new JSeparator(JSeparator.HORIZONTAL), c);

        // Status
        UIUtils.gbc(c, 0, 0, 100, 1, 0, 0, -1, true, false, 0, 0, 0, 0);
        logPanel.add(new JSeparator(JSeparator.HORIZONTAL), c);
        UIUtils.gbc(c, 0, 1, 1, 1, 1, 0, -1, true, false, 2, 1, 1, 1);
        logPanel.add(statusLbl, c);
        UIUtils.gbc(c, 1, 1, 1, 1, 0, 0, -1, false, false, 0, 1, 1, 1);
        logPanel.add(infoBtn, c);

        // Device Panel
        UIUtils.gbc(c, 0, 0, 1, 1, 1, 1, GridBagConstraints.WEST, true, true, 1, 1, 1, 1);
        deviceInfoPanel.add(deviceIdLbl, c);
        UIUtils.gbc(c, 0, 1, 1, 1, 0, 0, -1, true, false, 1, 0, 1, 1);
        deviceInfoPanel.add(typeLbl, c);
        UIUtils.gbc(c, 0, 2, 1, 1, 0, 0, -1, true, false, 1, 0, 1, 1);
        deviceInfoPanel.add(holderLbl, c);

        UIUtils.gbc(c, 0, 0, 1, 1, 0, 0, -1, true, false, 1, 1, 1, 1);
        deviceActionsPanel.add(readTagLbl, c);
        UIUtils.gbc(c, 1, 0, 4, 1, 1, 0, -1, true, false, 0, 1, 1, 1);
        deviceActionsPanel.add(readTagBtn, c);
        UIUtils.gbc(c, 0, 1, 5, 1, 1, 0, -1, true, false, 0, 0, 0, 1);
        deviceActionsPanel.add(new JSeparator(JSeparator.HORIZONTAL), c);
        UIUtils.gbc(c, 0, 2, 1, 1, 0, 0, -1, true, false, 1, 0, 1, 1);
        deviceActionsPanel.add(routeLbl, c);
        UIUtils.gbc(c, 1, 2, 4, 1, 1, 0, -1, true, false, 0, 0, 1, 1);
        deviceActionsPanel.add(routesCombo, c);
        UIUtils.gbc(c, 0, 3, 1, 1, 0, 0, -1, false, false, 1, 0, 1, 1);
        deviceActionsPanel.add(timeLbl, c);
        UIUtils.gbc(c, 1, 3, 1, 1, 1, 0, -1, true, false, 0, 0, 1, 1);
        deviceActionsPanel.add(timeField, c);
        UIUtils.gbc(c, 2, 3, 1, 1, 0, 0, -1, false, false, 0, 0, 0, 1);
        deviceActionsPanel.add(playBtn, c);
        UIUtils.gbc(c, 3, 3, 1, 1, 0, 0, -1, false, false, 0, 0, 0, 1);
        deviceActionsPanel.add(pauseBtn, c);
        UIUtils.gbc(c, 4, 3, 1, 1, 0, 0, -1, false, false, 0, 0, 1, 1);
        deviceActionsPanel.add(stopBtn, c);
        UIUtils.gbc(c, 0, 4, 5, 1, 1, 0, -1, true, false, 1, 0, 1, 1);
        deviceActionsPanel.add(seekbar, c);

        UIUtils.gbc(c, 0, 0, 1, 1, 1, 1, -1, true, true, 1, 1, 1, 1);
        deviceLogPanel.add(deviceLogSP, c);

        UIUtils.gbc(c, 0, 0, 1, 3, 1, 1, -1, true, true, 1, 1, 0, 1);
        devicePanel.add(deviceBox, c);
        UIUtils.gbc(c, 1, 0, 1, 1, 0, 0, -1, true, true, 1, 1, 1, 1);
        devicePanel.add(deviceInfoPanel, c);
        UIUtils.gbc(c, 1, 1, 1, 1, 0, 0, -1, true, true, 1, 0, 1, 1);
        devicePanel.add(deviceActionsPanel, c);
        UIUtils.gbc(c, 1, 2, 1, 1, 0, 1, -1, true, true, 1, 0, 1, 1);
        devicePanel.add(deviceLogPanel, c);

        deviceInfoPanel.setBorder(BorderFactory.createTitledBorder("Info"));
        deviceActionsPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        deviceLogPanel.setBorder(BorderFactory.createTitledBorder("Log"));

        /* COMPONENTS SETUP ----------------------------------------- */
        headerLbl.setFont(UIUtils.FONT_SANS_SERIF.deriveFont(Font.BOLD, 20f));
        statusLbl.setFont(UIUtils.FONT_MONO);
        deviceLogArea.setFont(UIUtils.FONT_MONO);
        deviceLogArea.setEditable(false);
        deviceLogArea.setWrapStyleWord(true);
        deviceLogArea.setLineWrap(true);
        seekbar.setMin(0);

        /* WRAPPING UP ---------------------------------------------- */
        add(mainMenuPanel, BorderLayout.NORTH);
        add(devicePanel);
        add(logPanel, BorderLayout.SOUTH);

        pack();
        setMinimumSize(getSize());

        // Measurement
        // temp
        int extra = (int) (devicesCombo.getLocation().getX() - deviceInfoPanel.getLocation().getX());
        devicesCombo.setPreferredSize(new Dimension((int) (devicesCombo.getSize().getWidth() + extra - 2), devicesCombo.getPreferredSize().height));
        pack();

        /* LISTENERS ------------------------------------------------ */
        deviceBox.setDeviceBoxListener(this);
        addDeviceBtn.addActionListener(this);
        deleteDeviceBtn.addActionListener(this);
        editDeviceBtn.addActionListener(this);
        syncBtn.addActionListener(this);
        readTagBtn.addActionListener(this);
        playBtn.addActionListener(this);
        pauseBtn.addActionListener(this);
        stopBtn.addActionListener(this);
        infoBtn.addActionListener(this);

        devicesCombo.addItemListener(this);
        routesCombo.addItemListener(this);

        timeField.getDocument().addDocumentListener(this);
    }

    private void enableInside(JPanel container, boolean enable) {
        for (Component c : container.getComponents()) {
            if (c instanceof JPanel) {
                enableInside((JPanel) c, enable);
                continue;
            }
            c.setEnabled(enable);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (callback != null) {
            if (e.getSource() == addDeviceBtn) {
                callback.onButtonClick(BUTTON_ADD_DEVICE);
            } else if (e.getSource() == deleteDeviceBtn) {
                callback.onButtonClick(BUTTON_REMOVE_DEVICE);
            } else if (e.getSource() == editDeviceBtn) {
                callback.onButtonClick(BUTTON_EDIT_DEVICE);
            } else if (e.getSource() == syncBtn) {
                callback.onButtonClick(BUTTON_SYNC);
            } else if (e.getSource() == readTagBtn) {
                callback.onButtonClick(BUTTON_READ_TAG);
            } else if (e.getSource() == playBtn) {
                callback.onButtonClick(BUTTON_ROUTE_PLAY);
            } else if (e.getSource() == pauseBtn) {
                callback.onButtonClick(BUTTON_ROUTE_PAUSE);
            } else if (e.getSource() == stopBtn) {
                callback.onButtonClick(BUTTON_ROUTE_STOP);
            } else if (e.getSource() == infoBtn) {
                callback.onButtonClick(BUTTON_INFO);
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (callback != null && e.getStateChange() == ItemEvent.SELECTED) {
            if (e.getSource() == devicesCombo) {
                if (devicesCombo.getSelectedItem() instanceof Device) {
                    callback.onDeviceSelected((Device) devicesCombo.getSelectedItem());
                } else {
                    callback.onDeviceSelected(null);
                }
                devicesCombo.setToolTipText(devicesCombo.getSelectedItem().toString());
            } else if (e.getSource() == routesCombo) {
                if (routesCombo.getSelectedItem() instanceof Route) {
                    callback.onRouteSelected((Route) routesCombo.getSelectedItem());
                } else {
                    callback.onRouteSelected(null);
                }
                routesCombo.setToolTipText(routesCombo.getSelectedItem().toString());
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateSeconds();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateSeconds();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateSeconds();
    }

    public void updateSeconds() {
        if (callback != null) {
            String value = timeField.getText();
            try {
                callback.onRoutingTimeChanged(Integer.parseInt(value));
                updateStatus("Time set to " + value + "s", 5);
            } catch (NumberFormatException ex) {
                updateStatus("Invalid seconds!", 3);
                System.err.println("Invalid integer: " + ex.getMessage());
            }
        }
    }

}
