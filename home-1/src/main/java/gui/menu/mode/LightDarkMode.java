package gui.menu.mode;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class LightDarkMode extends JPanel {

    private boolean menuFull = true;
    private JButton buttonLight;
    private JButton buttonDark;
    private JButton buttonLighDark;

    public LightDarkMode() {
        init();
    }

    public void setMenuFull(boolean menuFull) {
        this.menuFull = menuFull;
        buttonLight.setVisible(menuFull);
        buttonDark.setVisible(menuFull);
        buttonLighDark.setVisible(!menuFull);
    }

    private void init() {
        setBorder(new EmptyBorder(2, 2, 2, 2));
        setLayout(new LightDarkModeLayout());
        putClientProperty(FlatClientProperties.STYLE, "arc:999; background:$Menu.lightdark.background");

        buttonLight = new JButton("Light");
        buttonDark = new JButton("Dark");
        buttonLighDark = new JButton("Toggle");

        buttonLighDark.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:999;"
                + "background:$Menu.lightdark.button.background;"
                + "foreground:$Menu.foreground;"
                + "focusWidth:0;"
                + "borderWidth:0;"
                + "innerFocusWidth:0");

        buttonDark.addActionListener((ActionEvent e) -> {
            changeMode(true);
        });
        buttonLight.addActionListener((ActionEvent e) -> {
            changeMode(false);
        });
        buttonLighDark.addActionListener((ActionEvent e) -> {
            changeMode(!FlatLaf.isLafDark());
        });

        add(buttonLight);
        add(buttonDark);
        add(buttonLighDark);
    }

    private void changeMode(boolean dark) {
        if (FlatLaf.isLafDark() == dark) return;
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            if (dark) {
                FlatMacDarkLaf.setup();
            } else {
                FlatMacLightLaf.setup();
            }
            FlatLaf.updateUI();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    private class LightDarkModeLayout implements LayoutManager {
        @Override public void addLayoutComponent(String name, Component comp) {}
        @Override public void removeLayoutComponent(Component comp) {}
        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(5, buttonDark.getPreferredSize().height + (menuFull ? 0 : 5));
        }
        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(0,0);
        }
        @Override
        public void layoutContainer(Container parent) {
            Insets insets = parent.getInsets();
            int x = insets.left;
            int y = insets.top;
            int gap = 5;
            int width = parent.getWidth() - (insets.left + insets.right);
            int height= parent.getHeight() - (insets.top + insets.bottom);

            if (menuFull) {
                int buttonWidth = (width - gap)/2;
                buttonLight.setBounds(x, y, buttonWidth, height);
                buttonDark.setBounds(x + buttonWidth + gap, y, buttonWidth, height);
            } else {
                buttonLighDark.setBounds(x, y, width, height);
            }
        }
    }
}
