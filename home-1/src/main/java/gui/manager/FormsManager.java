package gui.manager;

import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import org.example.App;

import javax.swing.*;
import java.awt.*;

public class FormsManager {
    private App application;
    private static FormsManager instance;

    public static FormsManager getInstance() {
        if (instance == null) {
            instance = new FormsManager();
        }
        return instance;
    }

    private FormsManager() {
    }

    public void initApplication(App application) {
        this.application = application;
    }

    // This shows the given component in the main Application frame
    public void showForm(JComponent form) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            application.setContentPane(form);
            application.revalidate();
            application.repaint();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }
}
