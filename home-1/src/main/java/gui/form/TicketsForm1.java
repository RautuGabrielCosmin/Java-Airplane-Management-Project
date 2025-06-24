package gui.form;

import com.formdev.flatlaf.FlatClientProperties;
import clientGui.RpcConnection;
import domain.Ticket;
import gui.table.ButtonAction;
import gui.table.CheckBoxTableHeaderRenderer;
import gui.table.TableHeaderAlignment;
import service.TicketService;
import service.SocketTicketService;

import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement; // fixes ComponentPlacement errors
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD form for Tickets, now wired over JSON-RPC on port 5000.
 */
public class TicketsForm1 extends JPanel {

    // ← switch to the socket-backed service
    private final TicketService ticketService = new SocketTicketService();

    // UI fields
    private JPanel      panel;
    private JScrollPane scroll;
    private JTable      table;
    private JSeparator  separator;
    private JTextField  txtSearch;
    private JLabel      lbTitle;
    private ButtonAction cmdNew, cmdEdit, cmdDelete;

    public TicketsForm1() {
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

        /* ---------- table model ------------------------------------------- */
        table.setModel(new DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "SELECT", "#", "FLIGHT_ID", "TOURIST_ID", "EMPLOYEE_ID",
                        "SEAT_TYPE", "SEAT_NUMBER", "CUSTOMER_NAME"
                }
        ) {
            final Class<?>[] types = {
                    Boolean.class, Integer.class, Integer.class, Integer.class,
                    Integer.class, String.class,  String.class,  String.class
            };
            @Override public Class<?> getColumnClass(int c)      { return types[c]; }
            @Override public boolean  isCellEditable(int r,int c){ return c == 0; }
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
        lbTitle.setText("TICKETS");

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
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "height:30; separatorColor:$TableHeader.background; font:bold;");
        table.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30; showHorizontalLines:true; intercellSpacing:0,1; "
                        + "selectionBackground:$TableHeader.hoverBackground;");
        scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "trackArc:999; background:$Table.background;");
        txtSearch.putClientProperty(FlatClientProperties.STYLE,
                "arc:15; borderWidth:0; margin:5,20,5,20; background:$Panel.background;");
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Search...");
        table.getColumnModel().getColumn(0)
                .setHeaderRenderer(new CheckBoxTableHeaderRenderer(table, 0));
        table.getTableHeader()
                .setDefaultRenderer(new TableHeaderAlignment(table));
    }

    /**
     * Reloads the table from the RPC service.
     * All IOExceptions are caught and reported.
     */
    public void reloadData() {
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);
        try {
            for (Ticket t : ticketService.findAllTickets()) {
                m.addRow(new Object[] {
                        false,
                        t.getId(),
                        t.getFlightId(),
                        t.getTouristId(),
                        t.getEmployeeId(),
                        t.getSeatType(),
                        t.getSeatNumber(),
                        t.getCustomerName()
                });
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "Error loading tickets:\n" + ioe.getMessage(),
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
            for (Ticket t : ticketService.findAllTickets()) {
                boolean ok = q.isEmpty()
                        || t.getCustomerName().toLowerCase().contains(q)
                        || String.valueOf(t.getId()).equals(q);
                if (ok) {
                    m.addRow(new Object[] {
                            false,
                            t.getId(),
                            t.getFlightId(),
                            t.getTouristId(),
                            t.getEmployeeId(),
                            t.getSeatType(),
                            t.getSeatNumber(),
                            t.getCustomerName()
                    });
                }
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "Error searching tickets:\n" + ioe.getMessage(),
                    "I/O Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cmdNew() {
        JFrame f = new JFrame("New Ticket");
        f.setContentPane(new TicketsCreate1(this));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void cmdEdit() {
        JOptionPane.showMessageDialog(this, "Edit not implemented – add as needed.");
    }

    private void cmdDelete() {
        int[] sel = table.getSelectedRows();
        if (sel.length == 0) {
            JOptionPane.showMessageDialog(this, "Select at least one row.");
            return;
        }

        List<Integer> ids = new ArrayList<>();
        for (int r : sel) {
            ids.add((Integer) table.getValueAt(r, 1));
        }

        if (JOptionPane.showConfirmDialog(this,
                "Delete " + ids.size() + " ticket(s)?",
                "Confirm",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        for (int id : ids) {
            try {
                ticketService.deleteTicket(id);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting ticket " + id + ":\n" + ioe.getMessage(),
                        "I/O Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        reloadData();
    }
}
