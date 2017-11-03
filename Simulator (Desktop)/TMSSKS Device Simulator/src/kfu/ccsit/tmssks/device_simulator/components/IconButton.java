package kfu.ccsit.tmssks.device_simulator.components;

import kfu.ccsit.tmssks.device_simulator.res.R;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class IconButton extends JButton {

    private static final Color HOVER_COLOR = new Color(0, 0, 0, .15f);
    private static final Color PRESS_COLOR = new Color(0, 0, 0, .3f);

    private boolean circle;
    
    public IconButton(String src) {
        this(src, 10);
    }

    public IconButton(String src, int padding) {
        this(src, padding, false);
    }
    public IconButton(String src, int padding, boolean circle) {
        super.setIcon(new ImageIcon(R.get(src)));
        super.setBorderPainted(false);
        super.setContentAreaFilled(false);
        super.setFocusPainted(false);
        super.setOpaque(false);
        super.setPreferredSize(new Dimension(super.getIcon().getIconWidth() + padding,
                super.getIcon().getIconHeight() + padding));
        this.circle = circle;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (g instanceof Graphics2D) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        if (getModel().isPressed()) {
            g.setColor(PRESS_COLOR);
        } else if (getModel().isRollover()) {
            g.setColor(HOVER_COLOR);
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, getWidth(), getHeight(), circle ? getWidth() : 9, circle ? getHeight() : 9);
        super.paintComponent(g);
    }
}
