package gui.form;

import com.formdev.flatlaf.FlatClientProperties;
import gui.table.CheckBoxTableHeaderRenderer;
import gui.table.TableHeaderAlignment;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import raven.popup.DefaultOption;
import raven.popup.GlassPanePopup;
import raven.popup.component.SimplePopupBorder;
import raven.toast.Notifications;
import service.EmployeeService;
import service.MemoryEmployeeService;
import repository.MemoryEmployee;
import domain.Employee;
import java.util.ArrayList;
import java.util.List;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import java.awt.Window;
import javax.swing.SwingUtilities;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

/**
 * A panel that lists all employees in a JTable,
 * lets you search, create/edit/delete them,
 * and automatically reloads when its containing window gains focus.
 */
public class EmployeeForm extends JPanel
        implements ActionListener, WindowFocusListener {

    private final EmployeeService employeeService;

    // UI components (NetBeans-generated initComponents())
    private JPanel panel;
    private JScrollPane scroll;
    private JTable table;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JLabel lbTitle;
    private gui.table.ButtonAction cmdNew;
    private gui.table.ButtonAction cmdEdit;
    private gui.table.ButtonAction cmdDelete;

    public EmployeeForm() {
        this.employeeService = new MemoryEmployeeService(new MemoryEmployee());
        initComponents();
        initStylingAndListeners();
    }

    /**
     * Override addNotify so we can attach our WindowFocusListener
     * to the containing window (JFrame) once this panel is added.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) {
            w.addWindowFocusListener(this);
        }
    }

    private void initStylingAndListeners() {
        // FlatLaf styling
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:25; background:$Table.background;");
        table.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "height:30; hoverBackground:null; pressedBackground:null; "
                        + "separatorColor:$TableHeader.background; font:bold;");
        table.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30; showHorizontalLines:true; intercellSpacing:0,1; "
                        + "cellFocusColor:$TableHeader.hoverBackground; "
                        + "selectionBackground:$TableHeader.hoverBackground; "
                        + "selectionForeground:$Table.foreground;");
        scroll.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE,
                "trackArc:999; trackInsets:3,3,3,3; thumbInsets:3,3,3,3; background:$Table.background;");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +5;");
        txtSearch.putClientProperty(FlatClientProperties.STYLE,
                "arc:15; borderWidth:0; focusWidth:0; innerFocusWidth:0; margin:5,20,5,20; background:$Panel.background;");
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");

        // header renderers
        table.getColumnModel().getColumn(0).setHeaderRenderer(new CheckBoxTableHeaderRenderer(table, 0));
        table.getTableHeader().setDefaultRenderer(new TableHeaderAlignment(table));

        // listeners
        txtSearch.addActionListener(this);
        cmdNew.addActionListener(this);
        cmdEdit.addActionListener(this);
        cmdDelete.addActionListener(this);

        // initial data load
        reloadData();
    }

    /**
     * Public so EmployeeCreate can call back after saving.
     */
    public void reloadData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        List<Employee> employees;
        try {
            employees = employeeService.findAll();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (Employee e : employees) {
            model.addRow(new Object[]{false, e.getIdEmployee(), e.getName()});
        }
    }

    /**
     * Public so EmployeeCreate can invoke getNextEmployeeId() & create().
     */
    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == txtSearch) {
            doSearch(txtSearch.getText().trim());
        } else if (src == cmdNew) {
            openCreateDialog();
        } else if (src == cmdEdit) {
            // TODO: implement edit logic
        } else if (src == cmdDelete) {
            // TODO: implement delete logic
        }
    }

    private void doSearch(String query) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        if (query.isEmpty()) {
            reloadData();
            return;
        }
        List<Employee> all;
        try {
            all = employeeService.findAll();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error searching: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (Employee e : all) {
            boolean matches = e.getName().toLowerCase().contains(query.toLowerCase())
                    || String.valueOf(e.getIdEmployee()).equals(query);
            if (matches) {
                model.addRow(new Object[]{false, e.getIdEmployee(), e.getName()});
            }
        }
    }

    private void openCreateDialog() {
        JFrame frame = new JFrame("Create Employee");
        frame.setContentPane(new EmployeeCreate(this));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override public void windowGainedFocus(WindowEvent e) { reloadData(); }
    @Override public void windowLostFocus(WindowEvent e)  { /* no-op */ }

    /** NetBeans-generated UI layout; DO NOT MODIFY **/
    @SuppressWarnings("unchecked")
    private void initComponents() {
        panel          = new JPanel();
        scroll         = new JScrollPane();
        table          = new JTable();
        jSeparator1    = new javax.swing.JSeparator();
        txtSearch      = new javax.swing.JTextField();
        lbTitle        = new javax.swing.JLabel();
        cmdNew         = new gui.table.ButtonAction();
        cmdEdit        = new gui.table.ButtonAction();
        cmdDelete      = new gui.table.ButtonAction();

        table.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"SELECT", "#", "NAME"}
        ) {
            Class[] types = new Class[]{Boolean.class, Integer.class, String.class};
            @Override public Class getColumnClass(int col) { return types[col]; }
        });
        table.getTableHeader().setReorderingAllowed(false);
        scroll.setViewportView(table);

        txtSearch.setText("");
        lbTitle.setText("EMPLOYEE");
        cmdNew.setText("New");
        cmdEdit.setText("Edit");
        cmdDelete.setText("Delete");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 892, Short.MAX_VALUE)
                        .addGroup(panelLayout.createSequentialGroup()
                                .addGap(100, 100, 100)
                                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(674, Short.MAX_VALUE))
                        .addComponent(jSeparator1)
                        .addGroup(panelLayout.createSequentialGroup()
                                .addGap(100,100,100)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE,257,javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmdNew).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdEdit).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdDelete).addGap(13,13,13))
        );
        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                                .addGap(10,10,10)
                                .addComponent(lbTitle)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmdNew).addComponent(cmdEdit).addComponent(cmdDelete))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }
}
