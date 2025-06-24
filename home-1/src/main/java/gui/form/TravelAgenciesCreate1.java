package gui.form;

import java.awt.Window;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import domain.TravelAgency;
import service.TravelAgencyService;
import service.MemoryTravelAgencyService;
import repository.MemoryTravelAgency;

public class TravelAgenciesCreate1 extends javax.swing.JPanel {

    private final TravelAgencyService service =
            new MemoryTravelAgencyService(new MemoryTravelAgency());

    private final TravelAgenciesForm1 parentForm;

    public TravelAgenciesCreate1(){ this(null); }
    public TravelAgenciesCreate1(TravelAgenciesForm1 p){ this.parentForm=p; initComponents(); }

    // ───────────────────────────────────────────────────────────────────────────
    @SuppressWarnings("unchecked")
    private void initComponents(){

        lbId   = new javax.swing.JLabel();
        lbName = new javax.swing.JLabel();
        lbNum  = new javax.swing.JLabel();
        lbBook = new javax.swing.JLabel();

        txtId   = new javax.swing.JTextField();
        txtName = new javax.swing.JTextField();
        txtNum  = new javax.swing.JTextField();
        txtBook = new javax.swing.JTextField();

        cmdSave = new javax.swing.JToggleButton();
        cmdExit = new javax.swing.JToggleButton();

        lbId  .setText("ID:");
        lbName.setText("Name:");
        lbNum .setText("Number of Employees:");
        lbBook.setText("Flights Booked:");

        cmdSave.setText("Save"); cmdSave.addActionListener(e->save());
        cmdExit.setText("Exit"); cmdExit.addActionListener(e->closeWindow());

        javax.swing.GroupLayout l=new javax.swing.GroupLayout(this);
        this.setLayout(l);
        l.setHorizontalGroup(
                l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(l.createSequentialGroup().addGap(70)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(l.createSequentialGroup()
                                                .addComponent(lbId).addGap(120)
                                                .addComponent(txtId,170,170,170))
                                        .addGroup(l.createSequentialGroup()
                                                .addComponent(lbName).addGap(80)
                                                .addComponent(txtName,170,170,170))
                                        .addGroup(l.createSequentialGroup()
                                                .addComponent(lbNum).addGap(18)
                                                .addComponent(txtNum,170,170,170))
                                        .addGroup(l.createSequentialGroup()
                                                .addComponent(lbBook).addGap(70)
                                                .addComponent(txtBook,170,170,170))
                                        .addGroup(l.createSequentialGroup()
                                                .addComponent(cmdSave,100,100,100).addGap(200)
                                                .addComponent(cmdExit,100,100,100)))
                                .addGap(70))
        );
        l.setVerticalGroup(
                l.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(l.createSequentialGroup().addGap(30)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbId).addComponent(txtId,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbName).addComponent(txtName,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbNum).addComponent(txtNum,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbBook).addComponent(txtBook,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30)
                                .addGroup(l.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cmdSave).addComponent(cmdExit))
                                .addGap(30))
        );
    }

    private void save(){
        try{
            int    id      = Integer.parseInt(txtId.getText().trim());
            String name    = txtName.getText().trim();
            int    numEmp  = Integer.parseInt(txtNum.getText().trim());
            String flights = txtBook.getText().trim();   // ← flightsBooked is **String**

            if(name.isEmpty())
                throw new IllegalArgumentException("Name cannot be empty.");

            TravelAgency a = service.createTravelAgency(id, name, numEmp, flights);

            JOptionPane.showMessageDialog(this,
                    "Created travel agency:\n" + a,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            if(parentForm!=null) parentForm.reloadData();
            closeWindow();

        }catch(Exception ex){
            JOptionPane.showMessageDialog(this,
                    "Error: "+ex.getMessage(),
                    "Create Travel Agency",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void closeWindow(){
        Window w = SwingUtilities.getWindowAncestor(this);
        if(w!=null) w.dispose();
    }

    private javax.swing.JLabel lbId,lbName,lbNum,lbBook;
    private javax.swing.JTextField txtId,txtName,txtNum,txtBook;
    private javax.swing.JToggleButton cmdSave,cmdExit;
}
