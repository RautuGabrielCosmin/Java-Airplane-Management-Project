package gui.form;

import com.formdev.flatlaf.FlatClientProperties;
import clientGui.RpcConnection;
import domain.Flight;
import gui.table.ButtonAction;
import gui.table.CheckBoxTableHeaderRenderer;
import gui.table.TableHeaderAlignment;
import service.FlightService;
import service.SocketFlightService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD form for Flights, wired over JSON-RPC on port 5000.
 */
public class FlightsForm1 extends javax.swing.JPanel {

    // ← now uses the socket-backed service
    private final FlightService flightService = new SocketFlightService();

    public FlightsForm1() {
        initComponents();
        initLookAndFeel();
        reloadData();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panel     = new javax.swing.JPanel();
        scroll    = new javax.swing.JScrollPane();
        table     = new javax.swing.JTable();
        separator = new javax.swing.JSeparator();
        txtSearch = new javax.swing.JTextField();
        lbTitle   = new javax.swing.JLabel();
        cmdNew    = new ButtonAction();
        cmdEdit   = new ButtonAction();
        cmdDelete = new ButtonAction();

        table.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "SELECT", "#", "LOCATION_DEPARTURE", "LOCATION_ARRIVAL",
                        "DATE_DEPARTURE", "DATE_ARRIVAL",
                        "NUMBER_OF_SEATS", "FLIGHT_COMPANY_ID"
                }
        ) {
            final Class<?>[] types = {
                    Boolean.class, Integer.class, String.class, String.class,
                    String.class,  String.class,  Integer.class, Integer.class
            };
            @Override public Class<?> getColumnClass(int c) { return types[c]; }
            @Override public boolean isCellEditable(int r,int c) { return c==0; }
        });
        table.getTableHeader().setReorderingAllowed(false);
        scroll.setViewportView(table);

        cmdNew.setText("New");
        cmdNew.addActionListener(e -> cmdNew());

        cmdEdit.setText("Edit");
        cmdEdit.addActionListener(e -> cmdEdit());

        cmdDelete.setText("Delete");
        cmdDelete.addActionListener(e -> cmdDelete());

        txtSearch.addActionListener(e -> search());
        lbTitle.setText("FLIGHTS");

        // layout with GroupLayout
        javax.swing.GroupLayout p = new javax.swing.GroupLayout(panel);
        panel.setLayout(p);
        p.setHorizontalGroup(
                p.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scroll)
                        .addGroup(p.createSequentialGroup()
                                .addGap(90)
                                .addGroup(p.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lbTitle)
                                        .addGroup(p.createSequentialGroup()
                                                .addComponent(txtSearch, 250, 250, 250)
                                                .addGap(200)
                                                .addComponent(cmdNew)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmdEdit)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cmdDelete)
                                        )
                                )
                                .addGap(90)
                        )
                        .addComponent(separator)
        );
        p.setVerticalGroup(
                p.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, p.createSequentialGroup()
                                .addGap(10)
                                .addComponent(lbTitle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(p.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdNew)
                                        .addComponent(cmdEdit)
                                        .addComponent(cmdDelete)
                                )
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(separator, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                        )
        );

        javax.swing.GroupLayout root = new javax.swing.GroupLayout(this);
        this.setLayout(root);
        root.setHorizontalGroup(
                root.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel)
        );
        root.setVerticalGroup(
                root.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel)
        );
    }

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
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,"Search...");

        table.getColumnModel().getColumn(0)
                .setHeaderRenderer(new CheckBoxTableHeaderRenderer(table,0));
        table.getTableHeader()
                .setDefaultRenderer(new TableHeaderAlignment(table));
    }

    /**
     * Reloads the table by pulling data from the FlightService.
     * Any IOException is caught and shown in a dialog.
     */
    public void reloadData() {
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);

        try {
            List<Flight> flights = flightService.findAll();
            for (Flight f : flights) {
                m.addRow(new Object[]{
                        false,
                        f.getId(),
                        f.getLocationDeparture(),
                        f.getLocationArrival(),
                        f.getDateDeparture(),
                        f.getDateArrival(),
                        f.getNumberOfSeats(),
                        f.getFlightCompanyId()
                });
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error loading flights:\n" + ioe.getMessage(),
                    "I/O Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Simple search filter over departure/arrival/id.
     */
    private void search() {
        String q = txtSearch.getText().trim().toLowerCase();
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);

        try {
            for (Flight f : flightService.findAll()) {
                boolean ok = q.isEmpty()
                        || f.getLocationDeparture().toLowerCase().contains(q)
                        || f.getLocationArrival().toLowerCase().contains(q)
                        || String.valueOf(f.getId()).equals(q);
                if (ok) {
                    m.addRow(new Object[]{
                            false,
                            f.getId(),
                            f.getLocationDeparture(),
                            f.getLocationArrival(),
                            f.getDateDeparture(),
                            f.getDateArrival(),
                            f.getNumberOfSeats(),
                            f.getFlightCompanyId()
                    });
                }
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error searching flights:\n" + ioe.getMessage(),
                    "I/O Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cmdNew() {
        JFrame f = new JFrame("New Flight");
        f.setContentPane(new FlightsCreate1(this));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void cmdEdit() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this,"Select one row to edit.");
            return;
        }

        // existing row values
        int    id        = (int)    table.getValueAt(r, 1);
        String curDep    = (String) table.getValueAt(r, 2);
        String curArr    = (String) table.getValueAt(r, 3);
        String curDdep   = (String) table.getValueAt(r, 4);
        String curDarr   = (String) table.getValueAt(r, 5);
        int    curSeats  = (int)    table.getValueAt(r, 6);
        int    curCompId = (int)    table.getValueAt(r, 7);

        // prompt new values
        String newDep = JOptionPane.showInputDialog(this,
                "Location departure:", curDep);
        if (newDep==null) return;

        String newArr = JOptionPane.showInputDialog(this,
                "Location arrival:", curArr);
        if (newArr==null) return;

        String newDdep = JOptionPane.showInputDialog(this,
                "Date departure (YYYY-MM-DD):", curDdep);
        if (newDdep==null) return;

        String newDarr = JOptionPane.showInputDialog(this,
                "Date arrival (YYYY-MM-DD):", curDarr);
        if (newDarr==null) return;

        String seatsStr = JOptionPane.showInputDialog(this,
                "Number of seats:", curSeats);
        if(seatsStr==null) return;

        String compStr = JOptionPane.showInputDialog(this,
                "Flight company ID:", curCompId);
        if(compStr==null) return;

        try {
            int newSeats  = Integer.parseInt(seatsStr.trim());
            int newCompId = Integer.parseInt(compStr.trim());

            flightService.updateFlight(
                    id,
                    newDep.trim(), newArr.trim(),
                    newDdep.trim(), newDarr.trim(),
                    newSeats, newCompId
            );

            reloadData();
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,
                    "Seats and Company-ID must be integers.");
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "Error updating flight:\n" + ioe.getMessage(),
                    "I/O Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cmdDelete() {
        int[] sel = table.getSelectedRows();
        if (sel.length == 0) {
            JOptionPane.showMessageDialog(this,"Select at least one row.");
            return;
        }

        List<Integer> ids = new ArrayList<>();
        for (int r : sel) {
            ids.add((int) table.getValueAt(r,1));
        }

        int opt = JOptionPane.showConfirmDialog(this,
                "Delete " + ids.size() + " flight(s)?",
                "Confirm",
                JOptionPane.YES_NO_OPTION);
        if (opt != JOptionPane.YES_OPTION) return;

        for (int id : ids) {
            try {
                flightService.deleteFlight(id);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting flight " + id + ":\n" + ioe.getMessage(),
                        "I/O Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        reloadData();
    }

    // UI fields
    private javax.swing.JPanel      panel;
    private javax.swing.JScrollPane scroll;
    private javax.swing.JTable      table;
    private javax.swing.JSeparator  separator;
    private javax.swing.JTextField  txtSearch;
    private javax.swing.JLabel      lbTitle;
    private ButtonAction            cmdNew, cmdEdit, cmdDelete;
}
