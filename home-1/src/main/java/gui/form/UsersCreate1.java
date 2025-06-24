package gui.form;

import domain.User;
import repository.MemoryUser;
import service.MemoryUserService;
import service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.SwingUtilities;

public class UsersCreate1 extends JPanel {

    private final UserService userService =
            new MemoryUserService(new MemoryUser());
    private final UsersForm1 parentForm;

    private final JLabel lbId   = new JLabel("ID:");
    private final JLabel lbName = new JLabel("Username:");
    private final JLabel lbPass = new JLabel("Password:");

    private final JTextField txtId   = new JTextField();
    private final JTextField txtName = new JTextField();
    private final JTextField txtPass = new JTextField();

    private final JButton cmdSave = new JButton("Save");
    private final JButton cmdExit = new JButton("Exit");

    public UsersCreate1(UsersForm1 parentForm) {
        this.parentForm = parentForm;
        initComponents();
    }

    private void initComponents() {
        cmdSave.addActionListener(e -> save());
        cmdExit.addActionListener(e -> closeWindow());

        GroupLayout g = new GroupLayout(this);
        setLayout(g);
        g.setAutoCreateGaps(true);
        g.setAutoCreateContainerGaps(true);

        g.setHorizontalGroup(
                g.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(g.createSequentialGroup()
                                .addGroup(g.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbId)
                                        .addComponent(lbName)
                                        .addComponent(lbPass))
                                .addGroup(g.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(txtId,   GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtName, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPass, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
                        )
                        .addGroup(g.createSequentialGroup()
                                .addComponent(cmdSave)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmdExit))
        );

        g.setVerticalGroup(
                g.createSequentialGroup()
                        .addGroup(g.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lbId)
                                .addComponent(txtId, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(g.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lbName)
                                .addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(g.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lbPass)
                                .addComponent(txtPass, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(g.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(cmdSave)
                                .addComponent(cmdExit))
        );
    }

    private void save() {
        try {
            int    id   = txtId.getText().trim().isEmpty()
                    ? 0
                    : Integer.parseInt(txtId.getText().trim());
            String name = txtName.getText().trim();
            String pass = txtPass.getText().trim();

            if (name.isBlank() || pass.isBlank()) {
                throw new IllegalArgumentException("Username and password cannot be blank.");
            }

            User u = userService.createUser(id, name, pass);

            JOptionPane.showMessageDialog(
                    this,
                    "Created user:\n" + u,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (parentForm != null) parentForm.reloadData();
            closeWindow();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ex.getMessage(),
                    "Create User",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void closeWindow() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) w.dispose();
    }
}
