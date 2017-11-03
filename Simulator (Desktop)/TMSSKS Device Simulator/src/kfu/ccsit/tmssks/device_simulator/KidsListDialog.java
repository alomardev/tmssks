package kfu.ccsit.tmssks.device_simulator;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import kfu.ccsit.tmssks.device_simulator.entities.Kid;
import kfu.ccsit.tmssks.device_simulator.utils.UIUtils;

public class KidsListDialog extends JDialog implements DocumentListener, ListSelectionListener, ActionListener {

    public interface KidsListDialogListener {

        void onSubmitKids(ArrayList<Kid> kids);
    }

    private ArrayList<Kid> kids;
    private ArrayList<Kid> selectedKids;

    private KidsListDialogListener callback;

    private JTextField nameField;
    private JTextField nidField;
    private JLabel statusLbl;
    private JButton negativeBtn;
    private JButton positiveBtn;
    private DefaultListModel<Kid> listModel;
    private JList<Kid> list;

    public KidsListDialog(ArrayList<Kid> kids, JFrame owner) {
        super(owner, true);
        this.kids = kids;
        selectedKids = new ArrayList<>();
        listModel = new DefaultListModel<>();
    }

    public void setKidsListDialogListener(KidsListDialogListener callback) {
        this.callback = callback;
    }

    public void setup() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Select Kids");
        setLayout(new GridBagLayout());

        JPanel footerPanel = new JPanel(new GridBagLayout());
        JLabel nameLbl = new JLabel("Find by name:");
        JLabel nidLbl = new JLabel("Find by national id:");
        nameField = new JTextField(25);
        nidField = new JTextField(25);
        statusLbl = new JLabel("...");
        negativeBtn = new JButton("Cancel");
        positiveBtn = new JButton("Submit");
        list = new JList<>(listModel);
        JScrollPane listSp = new JScrollPane(list, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        GridBagConstraints c = new GridBagConstraints();
        UIUtils.gbc(c, 0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, false, false, 2, 2, 1, 0);
        add(nameLbl, c);
        UIUtils.gbc(c, 1, 0, 1, 1, 1, 0, -1, true, false, 0, 2, 2, 0);
        add(nameField, c);
        UIUtils.gbc(c, 0, 1, 1, 1, 0, 0, -1, false, false, 2, 1, 1, 0);
        add(nidLbl, c);
        UIUtils.gbc(c, 1, 1, 1, 1, 1, 0, -1, true, false, 0, 1, 2, 1);
        add(nidField, c);

        UIUtils.gbc(c, 0, 2, 2, 1, 1, 1, -1, true, true, 2, 1, 2, 2);
        add(listSp, c);

        UIUtils.gbc(c, 0, 3, 2, 1, 1, 0, -1, true, false, 2, 0, 2, 2);
        add(footerPanel, c);

        UIUtils.gbc(c, 0, 0, 1, 1, 1, 0, -1, true, false, 0, 0, 1, 0);
        footerPanel.add(statusLbl, c);
        UIUtils.gbc(c, 1, 0, 1, 1, 0, 0, -1, false, false, 0, 0, 1, 0);
        footerPanel.add(negativeBtn, c);
        UIUtils.gbc(c, 2, 0, 1, 1, 0, 0, -1, false, false, 0, 0, 0, 0);
        footerPanel.add(positiveBtn, c);

        listSp.setPreferredSize(new Dimension(0, 300));
        list.setFont(UIUtils.FONT_SANS_SERIF.deriveFont(14f));

        nameField.getDocument().addDocumentListener(this);
        nidField.getDocument().addDocumentListener(this);
        list.addListSelectionListener(this);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        negativeBtn.addActionListener(this);
        positiveBtn.addActionListener(this);

        pack();
        setPreferredSize(getSize());
        setResizable(false);
        setMaximumSize(new Dimension(getSize().width, 0));
    }

    public void refresh() {
        listModel.clear();
        kids.forEach((kid) -> {
            listModel.addElement(kid);
        });
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateList(nameField.getText().toUpperCase(), nidField.getText());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateList(nameField.getText().toUpperCase(), nidField.getText());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateList(nameField.getText().toUpperCase(), nidField.getText());
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        selectedKids.clear();
        int i = 0;
        for (Kid kid : list.getSelectedValuesList()) {
            i++;
            selectedKids.add(kid);
        }
        statusLbl.setText(i > 0 ? i + " " + (i == 1 ? "kid" : "kids") + " selected" : "Nothing selected");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == positiveBtn) {
            if (callback != null) {
                callback.onSubmitKids(selectedKids);
            }
        } else if (e.getSource() == list) {

        }

        if (e.getSource() == negativeBtn || e.getSource() == positiveBtn) {
            dispose();
        }
    }

    private void updateList(String name, String nid) {
        ArrayList<Kid> filtered = new ArrayList<>();
        boolean searchNid = !nid.isEmpty();
        boolean searchName = !name.isEmpty();

        kids.forEach((kid) -> {
            if (searchName && searchNid) {
                if (kid.getName().toUpperCase().contains(name) && kid.getNationalId().contains(nid)) {
                    filtered.add(kid);
                }
            } else if (searchName) {
                if (kid.getName().toUpperCase().contains(name)) {
                    filtered.add(kid);
                }
            } else if (searchNid) {
                if (kid.getNationalId().contains(nid)) {
                    filtered.add(kid);
                }
            }
        });

        if (searchNid || searchName) {
            listModel.clear();
            filtered.forEach((kid) -> {
                listModel.addElement(kid);
            });
        } else {
            refresh();
        }
    }

}
