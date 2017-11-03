package kfu.ccsit.tmssks.device_simulator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import kfu.ccsit.tmssks.device_simulator.res.R;

public final class DeviceBox extends JComponent implements MouseListener {

    private static final int PREFERRED_WIDTH = 300;
    private static final int PREFERRED_HEIGHT = 350;

    public static interface DeviceBoxListener {

        void onPowerButtonClick(boolean isOn);

    }

    private int offsetX;
    private int offsetY;
    private boolean enableNetwork;

    private Thread thread;
    private DeviceBoxListener callback;
    private TwoStatesImage imageLedPower;
    private TwoStatesImage imageLedNetwork;
    private TwoStatesImage imagePowerButton;
    private ArrayList<TwoStatesImage> images;

    @SuppressWarnings("LeakingThisInConstructor")
    public DeviceBox() {
        imageLedPower = new TwoStatesImage(246, 25, "device_led_power_on.png", "device_led_power_off.png");
        imageLedNetwork = new TwoStatesImage(214, 25, "device_led_network_on.png", "device_led_network_off.png");
        imagePowerButton = new TwoStatesImage(148, 288, "device_power_on.png", "device_power_off.png");

        images = new ArrayList<>();
        images.add(new TwoStatesImage(10, 10, "device_box.png", null));
        images.add(imageLedPower);
        images.add(imageLedNetwork);
        images.add(imagePowerButton);

        addMouseListener(this);

        imagePowerButton.toggle(false);
        imageLedPower.toggle(false);
        imageLedNetwork.toggle(false);

    }

    public void setDeviceBoxListener(DeviceBoxListener callback) {
        this.callback = callback;
    }
    
    public boolean isRunning() {
        return imagePowerButton.isEnabled();
    }

    public void turnNetwork(boolean on) {
        enableNetwork = on;

        if (!on) {
            if (thread != null) {
                try {
                    thread.join();
                } catch (InterruptedException ex) {
                    System.err.println(ex.getMessage());
                }
            }

            imageLedNetwork.toggle(false);
            return;
        }

        thread = new Thread(() -> {
            for (int i = 0; enableNetwork && i < 5; i++) {
                imageLedNetwork.toggle();
                repaint();
                try {
                    Thread.sleep(200 + (100 * (imageLedNetwork.isEnabled() ? 0 : 1)));
                } catch (InterruptedException ex) {
                    System.err.println(ex.getMessage());
                }
            }
            imageLedNetwork.toggle(enableNetwork);
        });

        thread.start();
    }
    
    public void setRunning(boolean running) {
        if (thread != null) {
            enableNetwork = running;
            try {
                thread.join();
            } catch (InterruptedException ex) {
                System.err.println(ex.getMessage());
            }
        }
        imagePowerButton.toggle(running);
        imageLedPower.toggle(running);
        imageLedNetwork.toggle(running);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        offsetX = getWidth() / 2 - PREFERRED_WIDTH / 2;
        offsetY = getHeight() / 2 - PREFERRED_HEIGHT / 2;

        images.forEach((image) -> {
            g.drawImage(image.getImage(), offsetX + image.getX(), offsetY + image.getY(), this);
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isEnabled() && e.getX() + 3 <= offsetX + imagePowerButton.getX() + imagePowerButton.getImage().getWidth(this)
                && e.getX() + 3 >= offsetX + imagePowerButton.getX()
                && e.getY() <= offsetY + imagePowerButton.getY() + imagePowerButton.getImage().getHeight(this)
                && e.getY() >= offsetY + imagePowerButton.getY()) {
            imagePowerButton.toggle();
            imageLedPower.toggle(imagePowerButton.isEnabled());
            turnNetwork(imagePowerButton.isEnabled());
            repaint();

            if (callback != null) {
                callback.onPowerButtonClick(imagePowerButton.isEnabled());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private static class TwoStatesImage {

        Image image1, image2;
        boolean enabled;
        int x, y;

        public boolean isEnabled() {
            return enabled;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public TwoStatesImage(int x, int y, String url1, String url2) {
            this.x = x;
            this.y = y;
            try {
                this.image1 = ImageIO.read(R.get(url1));
                if (url2 != null) {
                    this.image2 = ImageIO.read(R.get(url2));
                }
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
            enabled = true;
        }

        public void toggle(boolean enabled) {
            this.enabled = enabled;
        }

        public void toggle() {
            this.enabled = !enabled;
        }

        public Image getImage() {
            return enabled ? image1 : image2;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    }
}
