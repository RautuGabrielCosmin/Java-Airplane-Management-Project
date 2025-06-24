package gui.form;

import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import domain.Tourist;
import service.TouristService;
import service.MemoryTouristService;
import repository.MemoryTourist;

public class TouristsCreate1 extends javax.swing.JPanel {

    /* ------------------------------------------------------------------ */
    private final TouristService touristService =
            new MemoryTouristService(new MemoryTourist());

    /** back‑reference to refresh the table after a successful save */
    private final TouristsForm1 parentForm;

    public TouristsCreate1()               { this(null); }
    public TouristsCreate1(TouristsForm1 p){ this.parentForm = p; initComponents(); }

    /* ─────────────────────────────────────────────────────────────────── */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        lbId   = new javax.swing.JLabel();
        lbName = new javax.swing.JLabel();

        txtId   = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();

        cmdSave = new javax.swing.JToggleButton();
        cmdExit = new javax.swing.JToggleButton();

        lbId  .setText("ID:");
        lbName.setText("Name:");

        cmdSave.setText("Save"); cmdSave.addActionListener(e -> save());
        cmdExit.setText("Exit"); cmdExit.addActionListener(e -> closeWindow());

        /* ---------- layout (unchanged) ---------------------------------- */
        javax.swing.GroupLayout l = new javax.swing.GroupLayout(this);
        this.setLayout(l);
        l.setHorizontalGroup(
                l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(l.createSequentialGroup().addGap(80)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(l.createSequentialGroup()
                                                .addComponent(lbId).addGap(80).addComponent(txtId ,170,170,170))
                                        .addGroup(l.createSequentialGroup()
                                                .addComponent(lbName).addGap(50).addComponent(txtName,170,170,170))
                                        .addGroup(l.createSequentialGroup()
                                                .addComponent(cmdSave,100,100,100).addGap(160).addComponent(cmdExit,100,100,100)))
                                .addGap(80))
        );
        l.setVerticalGroup(
                l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(l.createSequentialGroup().addGap(30)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbId ).addComponent(txtId ,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbName).addComponent(txtName,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cmdSave).addComponent(cmdExit))
                                .addGap(25))
        );
    }

    /* ------------------------------------------------------------------ */
    private void save() {
        try {
            int    id   = Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();

            if (name.isBlank())
                throw new IllegalArgumentException("Name cannot be empty.");

            Tourist t = touristService.createTourist(id, name);

            JOptionPane.showMessageDialog(this,
                    "Created tourist:\n" + t,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            if (parentForm != null) parentForm.reloadData();
            closeWindow();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Create Tourist",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeWindow() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) w.dispose();
    }

    /* ------------------------------------------------------------------ */
    private javax.swing.JLabel      lbId, lbName;
    private javax.swing.JTextField  txtId, txtName;
    private javax.swing.JToggleButton cmdSave, cmdExit;
}


