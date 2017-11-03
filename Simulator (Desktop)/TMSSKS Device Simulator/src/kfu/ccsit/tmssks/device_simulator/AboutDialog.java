package kfu.ccsit.tmssks.device_simulator;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import kfu.ccsit.tmssks.device_simulator.res.R;
import kfu.ccsit.tmssks.device_simulator.utils.UIUtils;

public class AboutDialog extends JDialog {

    public AboutDialog(JFrame owner) {
        super(owner);
    }

    public void setup() {

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("About");
        setLayout(new GridBagLayout());

        JLabel image = new JLabel(new ImageIcon(R.get("org_logo.png")));

        JTextPane label = new JTextPane();
        JScrollPane sp = new JScrollPane(label, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        label.setText("Tracker Simulator for TMSSKS is a simulation software that shows how the actual hardware should work and functional. It mimics the functionalities; which are: reading tags, a moving transportaion, and server communication.\n"
                + "\nThis software is one of the proposed outcomes for the graduation project titled \"Transport Monitoring System for School Kids Safety\" (TMSSKS). Sponsored by College of Computer Sciences and Information Technology, King Faisal University.\n"
                + "\n"
                + "Done by:\n"
                + "Abdulrahman Mohammed Al'omar\n"
                + "Ammar Al'abdulqader\n"
                + "Khalid Ali Alshehri\n"
                + "\n"
                + "Supervised by:\n"
                + "Dr. Shakeel Ahmed\n"
                + "\n"
                + "Committee members:\n"
                + "Dr. Mohammed Billal\n"
                + "Dr. Sayd Afaq Husain\n"
                + "\n"
                + "(Spring 2017)");

        GridBagConstraints c = new GridBagConstraints();
        UIUtils.gbc(c, 0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, true, true, 1, 2, 1, 1);
        add(image, c);
        UIUtils.gbc(c, 0, 1, 1, 1, 1, 1, -1, true, true, 1, 1, 1, 1);
        add(sp, c);

        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        label.setEditable(false);
        label.setFont(UIUtils.FONT_SERIF.deriveFont(18f));
        StyledDocument doc = label.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        setSize(new Dimension(500, 500));
        setResizable(false);
        setMinimumSize(getSize());
        setMaximumSize(getSize());
    }

}
