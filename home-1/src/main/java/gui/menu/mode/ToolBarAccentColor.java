package gui.menu.mode;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.LoggingFacade;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import gui.menu.Menu;

public class ToolBarAccentColor extends JPanel {

    private final Menu menu;
    private final JPopupMenu popup = new JPopupMenu();
    private JToolBar toolbar;
    private JToggleButton selectedButton;
    private boolean menuFull = true;

    private final String[] accentColorKeys = {
            "App.accent.default", "App.accent.blue", "App.accent.purple", "App.accent.red",
            "App.accent.orange", "App.accent.yellow", "App.accent.green"
    };
    private final String[] accentColorNames = {
            "Default", "Blue", "Purple", "Red", "Orange", "Yellow", "Green"
    };

    public ToolBarAccentColor(Menu menu) {
        this.menu = menu;
        init();
    }

    public boolean isMenuFull() {
        return menuFull;
    }

    public void setMenuFull(boolean menuFull) {
        this.menuFull = menuFull;
        removeAll();
        if (menuFull) {
            add(toolbar, BorderLayout.CENTER);
            popup.remove(toolbar);
        } else {
            add(selectedButton, BorderLayout.CENTER);
            popup.add(toolbar);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void show(Component com, int x, int y) {
        if (menu.getComponentOrientation().isLeftToRight()) {
            popup.show(com, x, y);
        } else {
            int px = toolbar.getPreferredSize().width + UIScale.scale(5);
            popup.show(com, -px, y);
        }
        SwingUtilities.updateComponentTreeUI(popup);
    }

    private void init() {
        setLayout(new BorderLayout());
        toolbar = new JToolBar();
        add(toolbar, BorderLayout.CENTER);

        putClientProperty(FlatClientProperties.STYLE, "background:$Menu.background");
        toolbar.putClientProperty(FlatClientProperties.STYLE, "background:$Menu.background");
        popup.putClientProperty(FlatClientProperties.STYLE, "background:$Menu.background; borderColor:$Menu.background;");

        selectedButton = new JToggleButton("Accent Colors");
        selectedButton.addActionListener((ActionEvent e) -> {
            int y = (selectedButton.getPreferredSize().height - (toolbar.getPreferredSize().height + UIScale.scale(10))) / 2;
            show(ToolBarAccentColor.this, (int) getWidth() + UIScale.scale(4), y);
        });

        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < accentColorNames.length; i++) {
            String key = accentColorKeys[i];
            JToggleButton tbutton = new JToggleButton(accentColorNames[i]);
            tbutton.setSelected(UIManager.getColor("Component.accentColor").equals(UIManager.getColor(key)));
            tbutton.addActionListener((ActionEvent e) -> {
                try {
                    colorAccentChanged(key);
                } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(ToolBarAccentColor.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            group.add(tbutton);
            toolbar.add(tbutton);
        }
    }

    private void colorAccentChanged(String colorKey) throws NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        if (popup.isVisible()) {
            popup.setVisible(false);
        }
        Color color = UIManager.getColor(colorKey);
        selectedButton.setText("Accent: " + colorKey);

        Class<? extends LookAndFeel> lafClass = UIManager.getLookAndFeel().getClass();
        try {
            FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", toHexCode(color)));
            FlatLaf.setup(lafClass.getDeclaredConstructor().newInstance());
            FlatLaf.updateUI();
        } catch (InstantiationException | IllegalAccessException ex) {
            LoggingFacade.INSTANCE.logSevere(null, ex);
        }
    }

    private String toHexCode(Color color) {
        if (color == null) color = Color.lightGray;
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}
