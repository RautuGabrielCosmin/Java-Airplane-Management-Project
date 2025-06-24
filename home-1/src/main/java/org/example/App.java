package org.example;

import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import clientGui.RpcConnection;
import clientGui.Broadcaster;
import clientGui.RpcListener;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gui.login.Login;
import gui.menu.MainForm;
import gui.manager.FormsManager;
import raven.toast.Notifications;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class App extends JFrame implements RpcListener {
    private static App app;
    private final MainForm mainForm;
    private final Login loginForm;

    public App() {
        initComponents();
        setSize(new Dimension(1366, 768));
        setLocationRelativeTo(null);

        mainForm  = new MainForm();
        loginForm = new Login();

        // start on login screen
        setContentPane(loginForm);
        getRootPane().putClientProperty(FlatClientProperties.FULL_WINDOW_CONTENT, true);
        Notifications.getInstance().setJFrame(this);

        // register to receive server push messages
        Broadcaster.addListener(this);
    }

    public static App getAppInstance() {
        return app;
    }

    public MainForm getMainForm() {
        return mainForm;
    }

    public static void showForm(Component component) {
        component.applyComponentOrientation(app.getComponentOrientation());
        app.mainForm.showForm(component);
    }

    public static void logout() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.loginForm);
        app.loginForm.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.loginForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void setSelectedMenu(int index, int subIndex) throws IOException {
        app.mainForm.setSelectedMenu(index, subIndex);
    }

    /**
     * This method is invoked whenever the server broadcasts a JSON‐RPC notification.
     */
    @Override
    public void onBroadcast(ObjectNode msg) {
        // Always update UI on the EDT
        SwingUtilities.invokeLater(() -> {
            // You can inspect msg.get("service") and msg.get("action") here
            // and decide which form (if any) needs to reload its data.
            // e.g.:
            // if( msg.get("service").asText().equals("employee") ) {
            //     mainForm.getEmployeeForm().reload();
            // }
        });
    }

    public static void main(String[] args) {
        // 1) install L&F
        FlatRobotoFont.install();
        FlatMacDarkLaf.setup();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 14));

        // 2) init our long‐lived RPC connection on port 5000
        try {
            RpcConnection.init("localhost", 5000);

            // 3) start the “push” thread
            Thread pushThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = RpcConnection.readLine()) != null) {
                        ObjectNode notification = (ObjectNode) RpcConnection.getMapper().readTree(line);
                        Broadcaster.fire(notification);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }, "RPC-Push-Thread");
            pushThread.setDaemon(true);
            pushThread.start();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Unable to connect to server on port 5000:\n" + ex.getMessage(),
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }

        // 4) launch Swing UI
        EventQueue.invokeLater(() -> {
            app = new App();
            FormsManager.getInstance().initApplication(app);
            app.setVisible(true);
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 723, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 506, Short.MAX_VALUE));
        pack();
    }
    // </editor-fold>
}
