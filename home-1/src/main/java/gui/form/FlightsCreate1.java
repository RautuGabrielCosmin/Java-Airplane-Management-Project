package gui.form;

import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import domain.Flight;
import service.FlightService;
import service.MemoryFlightService;
import repository.MemoryFlight;

public class FlightsCreate1 extends javax.swing.JPanel {

    private final FlightService flightService =
            new MemoryFlightService(new MemoryFlight());

    private final FlightsForm1 parentForm;

    public FlightsCreate1()                { this(null); }
    public FlightsCreate1(FlightsForm1 f) { this.parentForm = f; initComponents(); }

    // ───────────────────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private void initComponents() {

        lbId   = new javax.swing.JLabel();
        lbDep  = new javax.swing.JLabel();
        lbArr  = new javax.swing.JLabel();
        lbDdep = new javax.swing.JLabel();
        lbDarr = new javax.swing.JLabel();
        lbSeat = new javax.swing.JLabel();
        lbComp = new javax.swing.JLabel();

        txtId    = new javax.swing.JTextField();
        txtDep   = new javax.swing.JTextField();
        txtArr   = new javax.swing.JTextField();
        txtDdep  = new javax.swing.JTextField();
        txtDarr  = new javax.swing.JTextField();
        txtSeats = new javax.swing.JTextField();
        txtComp  = new javax.swing.JTextField();

        cmdSave = new javax.swing.JToggleButton();
        cmdExit = new javax.swing.JToggleButton();

        lbId  .setText("ID of flight:");
        lbDep .setText("Location Departure:");
        lbArr .setText("Location Arrival:");
        lbDdep.setText("Date Departure:");
        lbDarr.setText("Date Arrival:");
        lbSeat.setText("Number of Seats:");
        lbComp.setText("Flight Company ID:");

        cmdSave.setText("Save"); cmdSave.addActionListener(e->save());
        cmdExit.setText("Exit"); cmdExit.addActionListener(e->closeWindow());

        javax.swing.GroupLayout l = new javax.swing.GroupLayout(this);
        this.setLayout(l);
        l.setHorizontalGroup(
                l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(l.createSequentialGroup().addGap(60)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(l.createSequentialGroup()
                                                .addComponent(cmdSave,100,100,100)
                                                .addGap(240).addComponent(cmdExit,100,100,100))
                                        .addGroup(l.createSequentialGroup()
                                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,false)
                                                        .addComponent(lbId)   .addComponent(lbDep).addComponent(lbArr)
                                                        .addComponent(lbDdep) .addComponent(lbDarr)
                                                        .addComponent(lbSeat) .addComponent(lbComp))
                                                .addGap(30)
                                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,false)
                                                        .addComponent(txtId,170,170,170)
                                                        .addComponent(txtDep).addComponent(txtArr)
                                                        .addComponent(txtDdep).addComponent(txtDarr)
                                                        .addComponent(txtSeats).addComponent(txtComp))))
                                .addGap(60))
        );
        l.setVerticalGroup(
                l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(l.createSequentialGroup().addGap(25)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbId).addComponent(txtId,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbDep).addComponent(txtDep,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbArr).addComponent(txtArr,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbDdep).addComponent(txtDdep,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbDarr).addComponent(txtDarr,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbSeat).addComponent(txtSeats,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbComp).addComponent(txtComp,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cmdSave).addComponent(cmdExit))
                                .addGap(25))
        );
    }

    private void save() {
        try {
            int id           = Integer.parseInt(txtId.getText().trim());
            String dep       = txtDep.getText().trim();
            String arr       = txtArr.getText().trim();
            String ddep      = txtDdep.getText().trim();
            String darr      = txtDarr.getText().trim();
            int seats        = Integer.parseInt(txtSeats.getText().trim());
            int compId       = Integer.parseInt(txtComp.getText().trim());

            if(dep.isEmpty()||arr.isEmpty()||ddep.isEmpty()||darr.isEmpty())
                throw new IllegalArgumentException("Text fields must not be empty.");

            Flight f = flightService.createFlight(id,dep,arr,ddep,darr,seats,compId);

            JOptionPane.showMessageDialog(this,"Created flight:\n"+f,
                    "Success",JOptionPane.INFORMATION_MESSAGE);
            if(parentForm!=null) parentForm.reloadData();
            closeWindow();
        } catch(Exception ex){
            JOptionPane.showMessageDialog(this,"Error: "+ex.getMessage(),
                    "Create Flight",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void closeWindow(){
        Window w= SwingUtilities.getWindowAncestor(this);
        if(w!=null) w.dispose();
    }

    // ───────────────────────────────────────────────────────────────────────────
    private javax.swing.JLabel lbId,lbDep,lbArr,lbDdep,lbDarr,lbSeat,lbComp;
    private javax.swing.JTextField txtId,txtDep,txtArr,txtDdep,txtDarr,txtSeats,txtComp;
    private javax.swing.JToggleButton cmdSave,cmdExit;
}
