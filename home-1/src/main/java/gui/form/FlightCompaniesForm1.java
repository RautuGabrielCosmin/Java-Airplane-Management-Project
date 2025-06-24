package gui.form;

import com.formdev.flatlaf.FlatClientProperties;
import gui.table.ButtonAction;
import gui.table.CheckBoxTableHeaderRenderer;
import gui.table.TableHeaderAlignment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import domain.FlightCompany;
import repository.MemoryFlightCompany;
import service.FlightCompanyService;
import service.MemoryFlightCompanyService;

/**
 * CRUD table for flight‑companies (same behaviour/look as EmployeeForm).
 */
public class FlightCompaniesForm1 extends javax.swing.JPanel {

    // ───────────────────────────────────────────────────────────────────────────
    // services
    private final FlightCompanyService flightCompanyService =
            new MemoryFlightCompanyService(new MemoryFlightCompany());

    // ───────────────────────────────────────────────────────────────────────────
    // ctor
    public FlightCompaniesForm1() throws IOException {
        initComponents();
        initLookAndFeel();
        reloadData();
    }

    // ───────────────────────────────────────────────────────────────────────────
    // generated UI
    @SuppressWarnings("unchecked")
    private void initComponents() {

        panel       = new javax.swing.JPanel();
        scroll      = new javax.swing.JScrollPane();
        table       = new javax.swing.JTable();
        separator   = new javax.swing.JSeparator();
        txtSearch   = new javax.swing.JTextField();
        lbTitle     = new javax.swing.JLabel();
        cmdNew      = new ButtonAction();
        cmdEdit     = new ButtonAction();
        cmdDelete   = new ButtonAction();

        // ---------- table ----------
        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] { "SELECT", "#", "NAME", "LOCATION" }
        ) {
            final Class<?>[] types = new Class [] {
                    Boolean.class, Integer.class, String.class, String.class
            };
            public Class<?> getColumnClass(int columnIndex) { return types[columnIndex]; }
            public boolean isCellEditable(int r,int c){ return c==0; }
        });
        table.getTableHeader().setReorderingAllowed(false);
        scroll.setViewportView(table);

        // ---------- buttons ----------
        cmdNew.setText("New");
        cmdNew  .addActionListener(e -> cmdNewActionPerformed());
        cmdEdit.setText("Edit");
        cmdEdit .addActionListener(e -> cmdEditActionPerformed());
        cmdDelete.setText("Delete");
        cmdDelete.addActionListener(e -> {
            try {
                cmdDeleteActionPerformed();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // ---------- search ----------
        txtSearch.addActionListener(e -> {
            try {
                search();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        lbTitle.setText("FLIGHT COMPANIES");

        // ---------- layout ----------
        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scroll, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelLayout.createSequentialGroup()
                                .addGap(90,90,90)
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbTitle)
                                        .addGroup(panelLayout.createSequentialGroup()
                                                .addComponent(txtSearch, 250, 250, 250)
                                                .addGap(250,250,250)
                                                .addComponent(cmdNew,   javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmdEdit,  javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmdDelete,javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(90,90,90))
                        .addComponent(separator, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                                .addGap(10,10,10)
                                .addComponent(lbTitle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdNew)
                                        .addComponent(cmdEdit)
                                        .addComponent(cmdDelete))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0,0,0)
                                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout root = new javax.swing.GroupLayout(this);
        this.setLayout(root);
        root.setHorizontalGroup(root.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        root.setVerticalGroup(root.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
    }

    /** identical FlatLaf / header‑checkbox styling used in EmployeeForm */
    private void initLookAndFeel() {

        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:25; background:$Table.background;");

        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "height:30; hoverBackground:null; pressedBackground:null; "
                        + "separatorColor:$TableHeader.background; font:bold;");

        table.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30; showHorizontalLines:true; intercellSpacing:0,1; "
                        + "cellFocusColor:$TableHeader.hoverBackground; "
                        + "selectionBackground:$TableHeader.hoverBackground; "
                        + "selectionForeground:$Table.foreground;");

        scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "trackArc:999; trackInsets:3,3,3,3; thumbInsets:3,3,3,3; "
                        + "background:$Table.background;");

        txtSearch.putClientProperty(FlatClientProperties.STYLE,
                "arc:15; borderWidth:0; focusWidth:0; innerFocusWidth:0; "
                        + "margin:5,20,5,20; background:$Panel.background;");
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");

        table.getColumnModel().getColumn(0)
                .setHeaderRenderer(new CheckBoxTableHeaderRenderer(table, 0));
        table.getTableHeader()
                .setDefaultRenderer(new TableHeaderAlignment(table));
    }

    // ───────────────────────────────────────────────────────────────────────────
    // helpers
    public void reloadData() throws IOException {
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);
        for (FlightCompany fc : flightCompanyService.findAll()) {
            m.addRow(new Object[] { false,
                    fc.getIdFlight(),
                    fc.getName(),
                    fc.getLocation() });
        }
    }

    private void search() throws IOException {
        String q = txtSearch.getText().trim().toLowerCase();
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);

        for (FlightCompany fc : flightCompanyService.findAll()) {
            boolean ok = q.isEmpty()
                    || fc.getName()         .toLowerCase().contains(q)
                    || fc.getLocation()     .toLowerCase().contains(q)
                    || String.valueOf(fc.getIdFlight()).equals(q);
            if (ok)
                m.addRow(new Object[]{false,
                        fc.getIdFlight(),
                        fc.getName(),
                        fc.getLocation()});
        }
    }

    private void cmdNewActionPerformed() {
        JFrame f = new JFrame("New Flight Company");
        f.setContentPane(new FlightCompaniesCreate1(this));
        f.pack(); f.setLocationRelativeTo(null); f.setVisible(true);
    }

    private void cmdEditActionPerformed() {
        int r = table.getSelectedRow();
        if (r < 0) { msg("Select one row to edit."); return; }

        int id        = (int)    table.getValueAt(r, 1);
        String curN   = (String) table.getValueAt(r, 2);
        String curLoc = (String) table.getValueAt(r, 3);

        String newN = JOptionPane.showInputDialog(this, "New name:", curN);
        if (newN == null || newN.trim().isEmpty()) return;

        String newL = JOptionPane.showInputDialog(this, "New location:", curLoc);
        if (newL == null || newL.trim().isEmpty()) return;

        try {
            flightCompanyService.updateFlightCompany(id, newN.trim(), newL.trim());
            reloadData();
        } catch (Exception ex) { msg(ex.getMessage()); }
    }

    private void cmdDeleteActionPerformed() throws IOException {
        int[] sel = table.getSelectedRows();
        if (sel.length == 0) { msg("Select at least one row."); return; }

        List<Integer> ids = new ArrayList<>();
        for (int r : sel) ids.add((int) table.getValueAt(r, 1));

        if (JOptionPane.showConfirmDialog(this,
                "Delete " + ids.size() + " company(s)?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;

        for (int id : ids) flightCompanyService.deleteFlightCompany(id);
        reloadData();
    }

    private void msg(String s) { JOptionPane.showMessageDialog(this, s); }

    // ───────────────────────────────────────────────────────────────────────────
    // swing vars
    private javax.swing.JPanel        panel;
    private javax.swing.JScrollPane   scroll;
    private javax.swing.JTable        table;
    private javax.swing.JSeparator    separator;
    private javax.swing.JTextField    txtSearch;
    private javax.swing.JLabel        lbTitle;
    private ButtonAction              cmdNew, cmdEdit, cmdDelete;
}