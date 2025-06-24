package gui.form;

import com.formdev.flatlaf.FlatClientProperties;
import clientGui.RpcConnection;
import domain.User;
import gui.table.ButtonAction;
import gui.table.CheckBoxTableHeaderRenderer;
import gui.table.TableHeaderAlignment;
import service.UserService;
import service.SocketUserService;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;  // ← this import fixes ComponentPlacement
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CRUD form for Users, now wired over JSON‐RPC on port 5000.
 */
public class UsersForm1 extends JPanel {

    // use the socket-backed service
    private final UserService userService = new SocketUserService();

    public UsersForm1() {
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
                new String[]{"SELECT", "#", "USERNAME", "PASSWORD"}
        ) {
            final Class<?>[] t = {
                    Boolean.class, Integer.class, String.class, String.class
            };
            @Override public Class<?> getColumnClass(int c) { return t[c]; }
            @Override public boolean isCellEditable(int r, int c) { return c == 0; }
        });
        table.getTableHeader().setReorderingAllowed(false);
        scroll.setViewportView(table);

        cmdNew   .setText("New");
        cmdNew   .addActionListener(e -> cmdNew());
        cmdEdit  .setText("Edit");
        cmdEdit  .addActionListener(e -> msg("Edit not implemented."));
        cmdDelete.setText("Delete");
        cmdDelete.addActionListener(e -> cmdDelete());

        txtSearch.addActionListener(e -> search());
        lbTitle.setText("USERS");

        /* ───────────────────────────────────────────────────────────────── */
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

        table.getColumnModel().getColumn(0)
                .setHeaderRenderer(new CheckBoxTableHeaderRenderer(table, 0));
        table.getTableHeader()
                .setDefaultRenderer(new TableHeaderAlignment(table));

        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT,
                "Search...");
    }

    /**
     * Reloads the table from the RPC service.
     * All IOExceptions are caught and reported.
     */
    public void reloadData() {
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);

        try {
            List<User> users = userService.findAll();
            for (User u : users) {
                m.addRow(new Object[]{
                        false,
                        u.getId(),
                        u.getUsername(),
                        u.getPassword()
                });
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "Error loading users:\n" + ioe.getMessage(),
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
            for (User u : userService.findAll()) {
                boolean ok = q.isEmpty()
                        || u.getUsername().toLowerCase().contains(q)
                        || String.valueOf(u.getId()).equals(q);
                if (ok) {
                    m.addRow(new Object[]{
                            false,
                            u.getId(),
                            u.getUsername(),
                            u.getPassword()
                    });
                }
            }
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "Error searching users:\n" + ioe.getMessage(),
                    "I/O Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cmdNew() {
        JFrame f = new JFrame("New User");
        f.setContentPane(new UsersCreate1(this));
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void cmdDelete() {
        int[] sel = table.getSelectedRows();
        if (sel.length == 0) {
            msg("Select rows");
            return;
        }

        List<Integer> ids = new ArrayList<>();
        for (int r : sel) {
            ids.add((Integer) table.getValueAt(r, 1));
        }

        if (JOptionPane.showConfirmDialog(this,
                "Delete " + ids.size() + " user(s)?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        ) != JOptionPane.YES_OPTION) {
            return;
        }

        for (int id : ids) {
            try {
                userService.deleteUser(id);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting user " + id + ":\n" + ioe.getMessage(),
                        "I/O Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        reloadData();
    }

    private void msg(String s) {
        JOptionPane.showMessageDialog(this, s);
    }

    // ───────────────────────────────────────────────────────────────────────────
    // UI fields (must match initComponents)
    private JPanel      panel;
    private JScrollPane scroll;
    private JTable      table;
    private JSeparator  separator;
    private JTextField  txtSearch;
    private JLabel      lbTitle;
    private ButtonAction cmdNew, cmdEdit, cmdDelete;
}
