package gui.form;

import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import domain.FlightCompany;
import service.FlightCompanyService;
import service.MemoryFlightCompanyService;
import repository.MemoryFlightCompany;

/**
 * “Create Flight‑Company” panel – identical behaviour to EmployeeCreate.
 */
public class FlightCompaniesCreate1 extends javax.swing.JPanel {

    private final FlightCompanyService flightCompanyService =
            new MemoryFlightCompanyService(new MemoryFlightCompany());

    private final FlightCompaniesForm1 parentForm;

    // ───────────────────────────────────────────────────────────────────────────
    public FlightCompaniesCreate1(){ this(null); }
    public FlightCompaniesCreate1(FlightCompaniesForm1 parentForm) {
        this.parentForm = parentForm;
        initComponents();
    }

    // ───────────────────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtId   = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        txtLoc  = new javax.swing.JTextField();
        cmdSave = new javax.swing.JToggleButton();
        cmdExit = new javax.swing.JToggleButton();

        jLabel1.setText("ID of the Flight Company:");
        jLabel2.setText("Name of the Flight Company:");
        jLabel3.setText("Location of the Flight Company:");

        cmdSave.setText("Save");
        cmdSave.addActionListener(e -> save());

        cmdExit.setText("Exit");
        cmdExit.addActionListener(e -> closeWindow());

        txtId.addActionListener(e -> txtName.requestFocusInWindow());

        // ---- layout ----
        javax.swing.GroupLayout l = new javax.swing.GroupLayout(this);
        this.setLayout(l);

        l.setHorizontalGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(l.createSequentialGroup().addGap(60,60,60)
                        .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(l.createSequentialGroup()      // buttons
                                        .addComponent(cmdSave, 100,100,100)
                                        .addGap(250,250,250)
                                        .addComponent(cmdExit, 100,100,100))
                                .addGroup(l.createSequentialGroup()       // id
                                        .addComponent(jLabel1)
                                        .addGap(30,30,30)
                                        .addComponent(txtId,170,170,170))
                                .addGroup(l.createSequentialGroup()       // name
                                        .addComponent(jLabel2)
                                        .addGap(18,18,18)
                                        .addComponent(txtName,170,170,170))
                                .addGroup(l.createSequentialGroup()       // loc
                                        .addComponent(jLabel3)
                                        .addGap(18,18,18)
                                        .addComponent(txtLoc,170,170,170)))
                        .addGap(60,60,60))
        );

        l.setVerticalGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(l.createSequentialGroup().addGap(25,25,25)
                        .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20,20,20)
                        .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20,20,20)
                        .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(txtLoc, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30,30,30)
                        .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cmdSave)
                                .addComponent(cmdExit))
                        .addGap(25,25,25))
        );
    }

    // ───────────────────────────────────────────────────────────────────────────
    private void save() {
        try {
            int    id       = Integer.parseInt(txtId.getText().trim());
            String name     = txtName.getText().trim();
            String location = txtLoc.getText().trim();

            if (name.isEmpty() || location.isEmpty())
                throw new IllegalArgumentException("Name and Location must not be empty.");

            FlightCompany fc = flightCompanyService.createFlightCompany(id, name, location);

            JOptionPane.showMessageDialog(this,
                    "Created flight company:\n" + fc,
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            if (parentForm != null) parentForm.reloadData();
            closeWindow();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Create Flight Company", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeWindow() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) w.dispose();
    }

    // ───────────────────────────────────────────────────────────────────────────
    // swing vars
    private javax.swing.JLabel  jLabel1, jLabel2, jLabel3;
    private javax.swing.JTextField txtId, txtName, txtLoc;
    private javax.swing.JToggleButton cmdSave, cmdExit;
}
