package gui.form;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Window;
import java.io.IOException;

/**
 * A little form that lets you type a new employee name and Save/Exit.
 */
public class EmployeeCreate extends javax.swing.JPanel {

    private final EmployeeForm parentForm;

    public EmployeeCreate() {
        this.parentForm = null;
        initComponents();
    }

    public EmployeeCreate(EmployeeForm parentForm) {
        this.parentForm = parentForm;
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1      = new javax.swing.JLabel("Name");
        txtName      = new javax.swing.JTextField();
        btnSave      = new javax.swing.JToggleButton("Save");
        btnExit      = new javax.swing.JToggleButton("Exit");

        btnSave.addActionListener(evt -> saveEmployee());
        btnExit.addActionListener(evt -> closeWindow());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtName, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                                .addGap(50, 50, 50))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 187, Short.MAX_VALUE)
                                .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(50, 50, 50)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSave)
                                        .addComponent(btnExit))
                                .addGap(7,7,7))
        );
    }

    private void saveEmployee() {
        if (parentForm == null) {
            JOptionPane.showMessageDialog(this,
                    "No reference to parent form. Cannot save.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Name cannot be empty.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int nextId;
        try {
            nextId = parentForm.getEmployeeService().getNextEmployeeId();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error generating new ID: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            parentForm.getEmployeeService().createEmployee(nextId, name);
            JOptionPane.showMessageDialog(this,
                    "Successfully created employee ID=" + nextId,
                    "Created", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error saving employee: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        parentForm.reloadData();
        closeWindow();
    }

    private void closeWindow() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) {
            w.dispose();
        }
    }

    // Variables declaration
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtName;
    private javax.swing.JToggleButton btnSave;
    private javax.swing.JToggleButton btnExit;
    // End of variables declaration
}
