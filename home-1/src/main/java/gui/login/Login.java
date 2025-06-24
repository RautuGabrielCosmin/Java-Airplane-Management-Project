package gui.login;

import com.formdev.flatlaf.FlatClientProperties;
import domain.User;
import gui.manager.FormsManager;
import net.miginfocom.swing.MigLayout;
import repository.MemoryUser;
import service.MemoryUserService;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import org.example.App;

public class Login extends JPanel {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chRememberMe;
    private JButton cmdLogin;

    public Login() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 20", "[center]", "[center]"));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        chRememberMe = new JCheckBox("Remember me");
        cmdLogin = new JButton("Login");

        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 35 45 30 45", "fill,250:280"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:20;"
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%)");

        txtPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true");

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your username or email");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your password");

        JLabel lbTitle = new JLabel("Welcome back!");
        JLabel description = new JLabel("Please sign in to access your account");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        description.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:lighten(@foreground,30%);"
                + "[dark]foreground:darken(@foreground,30%)");

        cmdLogin.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            if(username.isEmpty() || password.isEmpty()){
                JOptionPane.showMessageDialog(this, "Please fill in username and password", "Login Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Create the user service
            MemoryUserService userService = new MemoryUserService(new MemoryUser());
            // Retrieve all users (or, better, implement a findByUsername method in your repository)
            List<User> users = userService.findAll();
            User foundUser = null;
            for(User u : users) {
                if(u.getUsername().equals(username) && u.getPassword().equals(password)) {
                    foundUser = u;
                    break;
                }
            }
            if(foundUser != null) {
                // Credentials are correct; proceed to main UI
                // Instead of disposing the entire frame, show the MainForm
                App.getAppInstance().setContentPane(App.getAppInstance().getMainForm());
                App.getAppInstance().revalidate();
                App.getAppInstance().repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(lbTitle);
        panel.add(description);
        panel.add(new JLabel("Username"), "gapy 8");
        panel.add(txtUsername);
        panel.add(new JLabel("Password"), "gapy 8");
        panel.add(txtPassword);
        panel.add(chRememberMe, "grow 0");
        panel.add(cmdLogin, "gapy 10");
        panel.add(createSignupLabel(), "gapy 10");

        add(panel);
    }

    private Component createSignupLabel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null");

        JButton cmdRegister = new JButton("<html><a href=\"#\">Sign up</a></html>");
        cmdRegister.putClientProperty(FlatClientProperties.STYLE, "border:3,3,3,3");
        cmdRegister.setContentAreaFilled(false);
        cmdRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdRegister.addActionListener(e -> {
            // Click on "Sign up" triggers transition to Register form
            FormsManager.getInstance().showForm(new gui.login.Register());
        });

        JLabel label = new JLabel("Don't have an account ?");
        label.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:lighten(@foreground,30%);"
                + "[dark]foreground:darken(@foreground,30%)");

        panel.add(label);
        panel.add(cmdRegister);
        return panel;
    }
}
