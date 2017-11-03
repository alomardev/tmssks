package kfu.ccsit.tmssks.device_simulator.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

public class SeekBar extends JComponent {

    private static final int LINE_HEIGHT = 2;
    private static final int CIRCLE_RADIUS = 6;
    private static final Color LINE_UNREACH_COLOR = Color.LIGHT_GRAY;
    private static final Color LINE_COLOR = Color.BLACK;
    private static final Color LINE_COLOR_DISABLED = Color.GRAY;
    private static final Color CIRCLE_COLOR = Color.RED;
    private static final Color CIRCLE_COLOR_DISABLED = new Color(200, 128, 130);

    private int min, max, current;

    public float getPercentage() {
        if (max - min == 0) {
            return 0;
        }
        return (float) (current - min) / (float) (max - min);
    }

    public void setPercentage(float percentage) {
        current = min + (int) (max * percentage);
        repaint();
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
        repaint();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        repaint();
        this.max = max;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int offsetY = getHeight() / 2 - CIRCLE_RADIUS;
        
        int reachable = getWidth() - CIRCLE_RADIUS * 2;
        
        g.setColor(LINE_UNREACH_COLOR);
        g.fillRect(0, getHeight() / 2 - LINE_HEIGHT / 2, getWidth(), LINE_HEIGHT);
        g.setColor(isEnabled() ? LINE_COLOR : LINE_COLOR_DISABLED);
        g.fillRect(CIRCLE_RADIUS, getHeight() / 2 - LINE_HEIGHT / 2, reachable, LINE_HEIGHT);
        
        int position = (int) (getPercentage() * reachable);
        g.setColor(isEnabled() ? CIRCLE_COLOR : CIRCLE_COLOR_DISABLED);
        g.fillOval(position, offsetY, CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(CIRCLE_RADIUS * 4, CIRCLE_RADIUS * 2);
    }
    
}
