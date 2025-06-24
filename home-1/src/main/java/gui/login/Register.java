package gui.login;

import com.formdev.flatlaf.FlatClientProperties;
import gui.component.PasswordStrengthStatus;
import gui.manager.FormsManager;
import net.miginfocom.swing.MigLayout;
import repository.MemoryUser;
import service.MemoryUserService;

import javax.swing.*;
import java.awt.*;

public class Register extends JPanel {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton cmdRegister;
    private PasswordStrengthStatus passwordStrengthStatus;

    public Register() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();
        cmdRegister = new JButton("Sign Up");

        passwordStrengthStatus = new PasswordStrengthStatus();

        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "[fill,360]"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%)");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Re-enter your password");

        txtPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, "showRevealButton:true");

        cmdRegister.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,10%);"
                + "[dark]background:lighten(@background,10%);"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0");

        JLabel lbTitle = new JLabel("Welcome to our Airport Application");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");

        passwordStrengthStatus.initPasswordField(txtPassword);

        cmdRegister.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String confirmPassword = new String(txtConfirmPassword.getPassword()).trim();

            if(username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username is empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if(password.isEmpty() || confirmPassword.isEmpty() || !password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match or are empty!", "Sign Up Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                // Just pass 0 as the ID; your service will figure out the next available
                MemoryUserService userService = new MemoryUserService(new MemoryUser());
                userService.createUser(0, username, password);

                JOptionPane.showMessageDialog(this, "Registration successful!", "Registration", JOptionPane.INFORMATION_MESSAGE);
                // Optionally, switch to Login form
                FormsManager.getInstance().showForm(new Login());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(lbTitle);
        panel.add(new JLabel("Username"));
        panel.add(txtUsername);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(passwordStrengthStatus, "gapy 0");
        panel.add(new JLabel("Confirm Password"), "gapy 0");
        panel.add(txtConfirmPassword);
        panel.add(cmdRegister, "gapy 20");
        panel.add(createLoginLabel(), "gapy 10");
        add(panel);
    }

    private Component createLoginLabel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null");

        JButton cmdLogin = new JButton("<html><a href=\"#\">Sign in here</a></html>");
        cmdLogin.putClientProperty(FlatClientProperties.STYLE, "border:3,3,3,3");
        cmdLogin.setContentAreaFilled(false);
        cmdLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdLogin.addActionListener(e -> {
            // Click on "Sign in here" triggers transition to Login form
            FormsManager.getInstance().showForm(new Login());
        });

        JLabel label = new JLabel("Already have an account ?");
        label.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:lighten(@foreground,30%);"
                + "[dark]foreground:darken(@foreground,30%)");

        panel.add(label);
        panel.add(cmdLogin);
        return panel;
    }

    //--- Changed logic to reflect the name: returns true if username is empty
    public boolean isUsernameEmpty() {
        String username = txtUsername.getText().trim();
        return username.isEmpty();
    }

    public boolean isMatchPassword() {
        String password = String.valueOf(txtPassword.getPassword());
        String confirmPassword = String.valueOf(txtConfirmPassword.getPassword());
        return !password.isEmpty() && password.equals(confirmPassword);
    }
}
