package gui.form;

import com.formdev.flatlaf.FlatClientProperties;
import clientGui.RpcConnection;
import domain.TravelAgency;
import gui.table.ButtonAction;
import gui.table.CheckBoxTableHeaderRenderer;
import gui.table.TableHeaderAlignment;
import service.TravelAgencyService;
import service.SocketTravelAgencyService;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;  // <— fixes ComponentPlacement
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD form for Travel Agencies over JSON-RPC on port 5000.
 */
public class TravelAgenciesForm1 extends JPanel {

    // ← now uses the socket-backed implementation
    private final TravelAgencyService service = new SocketTravelAgencyService();

    // UI components
    private JPanel      panel;
    private JScrollPane scroll;
    private JTable      table;
    private JSeparator  separator;
    private JTextField  txtSearch;
    private JLabel      lbTitle;
    private ButtonAction cmdNew, cmdEdit, cmdDelete;

    public TravelAgenciesForm1() {
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

        /* ------------ correct column types (last column is String) -------- */
        table.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[]{"SELECT","#","NAME","NUMBER_OF_EMPLOYEES","FLIGHTS_BOOKED"}
        ) {
            final Class<?>[] t = {
                    Boolean.class, Integer.class, String.class, Integer.class, String.class
            };
            @Override public Class<?> getColumnClass(int c) { return t[c]; }
            @Override public boolean isCellEditable(int r,int c) { return c==0; }
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
        lbTitle.setText("TRAVEL AGENCIES");

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
                                                .addGap(160)
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
                                        .addComponent(txtSearch, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
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

        table.getColumnModel().getColumn(0)
                .setHeaderRenderer(new CheckBoxTableHeaderRenderer(table, 0));
        table.getTableHeader()
                .setDefaultRenderer(new TableHeaderAlignment(table));

        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Search...");
    }

    /**
     * Reload all agencies (I/O exceptions are caught and shown).
     */
    public void reloadData() {
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);

        try {
            for (TravelAgency a : service.findAll()) {
                m.addRow(new Object[]{
                        false,
                        a.getIdTravelAgency(),
                        a.getName(),
                        a.getNumberOfEmployees(),
                        a.getFlightsBooked()
                });
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "Error loading agencies:\n" + ioe.getMessage(),
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
            for (TravelAgency a : service.findAll()) {
                boolean ok = q.isEmpty()
                        || a.getName().toLowerCase().contains(q)
                        || String.valueOf(a.getIdTravelAgency()).equals(q);
                if (ok) {
                    m.addRow(new Object[]{
                            false,
                            a.getIdTravelAgency(),
                            a.getName(),
                            a.getNumberOfEmployees(),
                            a.getFlightsBooked()
                    });
                }
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "Error searching agencies:\n" + ioe.getMessage(),
                    "I/O Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cmdNew() {
        JFrame f = new JFrame("New Travel Agency");
        f.setContentPane(new TravelAgenciesCreate1(this));
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
            JOptionPane.showMessageDialog(this, "Select rows");
            return;
        }

        List<Integer> ids = new ArrayList<>();
        for (int r : sel) {
            ids.add((Integer) table.getValueAt(r, 1));
        }

        if (JOptionPane.showConfirmDialog(this,
                "Delete " + ids.size() + " agency(s)?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION
        ) {
            return;
        }

        for (int id : ids) {
            try {
                service.deleteTravelAgency(id);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting agency " + id + ":\n" + ioe.getMessage(),
                        "I/O Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        reloadData();
    }
}
