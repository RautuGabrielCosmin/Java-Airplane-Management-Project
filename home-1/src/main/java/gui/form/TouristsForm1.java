package gui.form;

import com.formdev.flatlaf.FlatClientProperties;
import clientGui.RpcConnection;
import domain.Tourist;
import gui.table.ButtonAction;
import gui.table.CheckBoxTableHeaderRenderer;
import gui.table.TableHeaderAlignment;
import service.TouristService;
import service.SocketTouristService;

import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement; // fixes ComponentPlacement errors
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD form for Tourists, now wired over JSON-RPC on port 5000.
 */
public class TouristsForm1 extends JPanel {

    // ← switch to the socket-backed service
    private final TouristService touristService = new SocketTouristService();

    // UI components
    private JPanel      panel;
    private JScrollPane scroll;
    private JTable      table;
    private JSeparator  separator;
    private JTextField  txtSearch;
    private JLabel      lbTitle;
    private ButtonAction cmdNew, cmdEdit, cmdDelete;

    public TouristsForm1() {
        initComponents();
        initLookAndFeel();
        reloadData();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panel     = new JPanel();
        scroll    = new JScrollPane();
        table     = new JTable();
        separator = new JSeparator();
        txtSearch = new JTextField();
        lbTitle   = new JLabel();
        cmdNew    = new ButtonAction();
        cmdEdit   = new ButtonAction();
        cmdDelete = new ButtonAction();

        /* ---------- table model ---------------------------------------- */
        table.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"SELECT", "#", "NAME"}
        ) {
            final Class<?>[] t = { Boolean.class, Integer.class, String.class };
            @Override public Class<?> getColumnClass(int c) { return t[c]; }
            @Override public boolean isCellEditable(int r, int c) { return c == 0; }
        });
        table.getTableHeader().setReorderingAllowed(false);
        scroll.setViewportView(table);

        cmdNew   .setText("New");
        cmdNew   .addActionListener((ActionEvent e) -> cmdNew());
        cmdEdit  .setText("Edit");
        cmdEdit  .addActionListener((ActionEvent e) -> cmdEdit());
        cmdDelete.setText("Delete");
        cmdDelete.addActionListener((ActionEvent e) -> cmdDelete());

        txtSearch.addActionListener(e -> search());
        lbTitle.setText("TOURISTS");

        GroupLayout p = new GroupLayout(panel);
        panel.setLayout(p);
        p.setHorizontalGroup(
                p.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scroll)
                        .addGroup(p.createSequentialGroup()
                                .addGap(90)
                                .addGroup(p.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(lbTitle)
                                        .addGroup(p.createSequentialGroup()
                                                .addComponent(txtSearch, 250, 250, 250)
                                                .addGap(200)
                                                .addComponent(cmdNew)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(cmdEdit)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(cmdDelete)
                                        )
                                )
                                .addGap(90)
                        )
                        .addComponent(separator)
        );
        p.setVerticalGroup(
                p.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, p.createSequentialGroup()
                                .addGap(10)
                                .addComponent(lbTitle)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(p.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdNew)
                                        .addComponent(cmdEdit)
                                        .addComponent(cmdDelete)
                                )
                                .addPreferredGap(ComponentPlacement.UNRELATED)
                                .addComponent(separator, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(scroll, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                        )
        );

        GroupLayout root = new GroupLayout(this);
        this.setLayout(root);
        root.setHorizontalGroup(
                root.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panel)
        );
        root.setVerticalGroup(
                root.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panel)
        );
    }

    private void initLookAndFeel() {
        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:25; background:$Table.background;");

        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "height:30; font:bold;");
        table.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30; intercellSpacing:0,1;");
        table.getColumnModel().getColumn(0)
                .setHeaderRenderer(new CheckBoxTableHeaderRenderer(table, 0));
        table.getTableHeader()
                .setDefaultRenderer(new TableHeaderAlignment(table));

        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Search...");
    }

    /**
     * Reloads the table from the RPC service.
     * IOExceptions are caught and shown.
     */
    public void reloadData() {
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);
        try {
            for (Tourist t : touristService.findAll()) {
                m.addRow(new Object[]{ false, t.getIdTourist(), t.getName() });
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "Error loading tourists:\n" + ioe.getMessage(),
                    "I/O Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void search() {
        String q = txtSearch.getText().trim().toLowerCase();
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);
        try {
            for (Tourist t : touristService.findAll()) {
                boolean ok = q.isEmpty()
                        || t.getName().toLowerCase().contains(q)
                        || String.valueOf(t.getIdTourist()).equals(q);
                if (ok) {
                    m.addRow(new Object[]{ false, t.getIdTourist(), t.getName() });
                }
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "Error searching tourists:\n" + ioe.getMessage(),
                    "I/O Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cmdNew() {
        JFrame f = new JFrame("New Tourist");
        f.setContentPane(new TouristsCreate1(this));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void cmdEdit() {
        JOptionPane.showMessageDialog(this, "Edit not implemented.");
    }

    private void cmdDelete() {
        int[] sel = table.getSelectedRows();
        if (sel.length == 0) {
            JOptionPane.showMessageDialog(this, "Select row(s)");
            return;
        }

        List<Integer> ids = new ArrayList<>();
        for (int r : sel) {
            ids.add((Integer) table.getValueAt(r, 1));
        }

        if (JOptionPane.showConfirmDialog(this,
                "Delete " + ids.size() + " tourist(s)?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        for (int id : ids) {
            try {
                touristService.deleteTourist(id);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting tourist " + id + ":\n" + ioe.getMessage(),
                        "I/O Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        reloadData();
    }
}
