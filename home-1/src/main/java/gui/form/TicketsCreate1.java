package gui.form;

import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import domain.Ticket;
import service.TicketService;
import service.MemoryTicketService;
import repository.MemoryTicket;

public class TicketsCreate1 extends javax.swing.JPanel {

    /* ------------------------------------------------------------------ */
    private final TicketService ticketService =
            new MemoryTicketService(new MemoryTicket());

    private final TicketsForm1 parentForm;

    public TicketsCreate1()            { this(null); }
    public TicketsCreate1(TicketsForm1 p) { this.parentForm = p; initComponents(); }

    /* ------------------------------------------------------------------ */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        lbFlight  = new javax.swing.JLabel();
        lbTourist = new javax.swing.JLabel();
        lbEmp     = new javax.swing.JLabel();
        lbType    = new javax.swing.JLabel();
        lbNumber  = new javax.swing.JLabel();
        lbName    = new javax.swing.JLabel();

        txtFlight  = new javax.swing.JTextField();
        txtTourist = new javax.swing.JTextField();
        txtEmp     = new javax.swing.JTextField();
        txtType    = new javax.swing.JTextField();
        txtNumber  = new javax.swing.JTextField();
        txtName    = new javax.swing.JTextField();

        cmdSave = new javax.swing.JToggleButton();
        cmdExit = new javax.swing.JToggleButton();

        lbFlight .setText("Flight ID:");
        lbTourist.setText("Tourist ID:");
        lbEmp    .setText("Employee ID:");
        lbType   .setText("Seat Type:");
        lbNumber .setText("Seat Number:");
        lbName   .setText("Customer Name:");

        cmdSave.setText("Save");
        cmdSave.addActionListener(e -> save());
        cmdExit.setText("Exit");
        cmdExit.addActionListener(e -> closeWindow());

        javax.swing.GroupLayout l = new javax.swing.GroupLayout(this);
        this.setLayout(l);
        l.setHorizontalGroup(
                l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(l.createSequentialGroup().addGap(60)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(l.createSequentialGroup()
                                                .addComponent(cmdSave,100,100,100)
                                                .addGap(240)
                                                .addComponent(cmdExit,100,100,100))
                                        .addGroup(l.createSequentialGroup()
                                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,false)
                                                        .addComponent(lbFlight).addComponent(lbTourist)
                                                        .addComponent(lbEmp).addComponent(lbType)
                                                        .addComponent(lbNumber).addComponent(lbName))
                                                .addGap(40)
                                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,false)
                                                        .addComponent(txtFlight,170,170,170)
                                                        .addComponent(txtTourist).addComponent(txtEmp)
                                                        .addComponent(txtType).addComponent(txtNumber)
                                                        .addComponent(txtName))))
                                .addGap(60))
        );
        l.setVerticalGroup(
                l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(l.createSequentialGroup().addGap(25)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbFlight)
                                        .addComponent(txtFlight,javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbTourist)
                                        .addComponent(txtTourist,javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbEmp)
                                        .addComponent(txtEmp,javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbType)
                                        .addComponent(txtType,javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbNumber)
                                        .addComponent(txtNumber,javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbName)
                                        .addComponent(txtName,javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cmdSave)
                                        .addComponent(cmdExit))
                                .addGap(25))
        );
    }

    /* ------------------------------------------------------------------ */
    private void save() {
        try {
            int flight   = Integer.parseInt(txtFlight .getText().trim());
            int tourist  = Integer.parseInt(txtTourist.getText().trim());
            int emp      = Integer.parseInt(txtEmp    .getText().trim());
            String type  = txtType  .getText().trim();
            String number= txtNumber.getText().trim();    // seatNumber is **String**
            String name  = txtName  .getText().trim();

            if (type.isBlank() || number.isBlank() || name.isBlank())
                throw new IllegalArgumentException("All text fields must be filled.");

            /* 0 → let the service choose the next available id (see service implementation) */
            Ticket t = ticketService.createTicket(0, flight, tourist, emp, type, number, name);

            JOptionPane.showMessageDialog(this, "Created ticket:\n" + t,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            if (parentForm != null) parentForm.reloadData();
            closeWindow();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Create Ticket", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeWindow() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) w.dispose();
    }

    /* ------------------------------------------------------------------ */
    private javax.swing.JLabel      lbFlight, lbTourist, lbEmp, lbType, lbNumber, lbName;
    private javax.swing.JTextField  txtFlight, txtTourist, txtEmp, txtType, txtNumber, txtName;
    private javax.swing.JToggleButton cmdSave, cmdExit;
}
