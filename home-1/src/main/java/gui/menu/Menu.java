package gui.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import gui.menu.mode.LightDarkMode;
import gui.menu.mode.ToolBarAccentColor;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Window;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.example.App;

public class Menu extends JPanel {

    private final String menuItems[][] = {
            {"~MAIN~"},
            {"Tables", "Employees", "Flights", "Flight Companies", "Tickets", "Tourists",
                    "Travel Agencies", "Users"},
            {"~ACTIONS~"},
            {"Purchase"},
            {"Logout"}
    };

    private boolean menuFull = true;
    protected final boolean hideMenuTitleOnMinimum = true;
    protected final int menuTitleLeftInset = 5;
    protected final int menuTitleVgap = 5;
    protected final int menuMaxWidth = 250;
    protected final int menuMinWidth = 60;
    protected final int headerFullHgap = 5;

    private JLabel header;
    private JScrollPane scroll;
    private JPanel panelMenu;
    private LightDarkMode lightDarkMode;
    private ToolBarAccentColor toolBarAccentColor;
    private final List<MenuEvent> events = new ArrayList<>();

    public Menu() {
        init();
    }

    private void init() {
        setLayout(new MenuLayout());
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:20,2,2,2;"
                + "background:$Menu.background;"
                + "arc:10");

        header = new JLabel("Menu");
        header.putClientProperty(FlatClientProperties.STYLE, "font:$Menu.label.font; foreground:$Menu.title.foreground");

        scroll = new JScrollPane();
        panelMenu = new JPanel(new MenuItemLayout(this));
        panelMenu.putClientProperty(FlatClientProperties.STYLE, "border:5,5,5,5; background:$Menu.background");

        scroll.setViewportView(panelMenu);
        // Remove "thumb" style to fix the error
        scroll.putClientProperty(FlatClientProperties.STYLE,
                "border:null;"
                        + "background:$Menu.ScrollBar.background;");

        createMenu();

        lightDarkMode = new LightDarkMode();
        toolBarAccentColor = new ToolBarAccentColor(this);
        toolBarAccentColor.setVisible(FlatUIUtils.getUIBoolean("AccentControl.show", false));

        add(header);
        add(scroll);
        add(lightDarkMode);
        add(toolBarAccentColor);
    }

    private void createMenu() {
        int index = 0;
        for (String[] item : menuItems) {
            String menuName = item[0];
            if (menuName.startsWith("~") && menuName.endsWith("~")) {
                panelMenu.add(createTitle(menuName));
            } else {
                MenuItem menuItem = new MenuItem(this, item, index++, events);
                panelMenu.add(menuItem);
            }
        }
    }

    private JLabel createTitle(String title) {
        String menuName = title.substring(1, title.length() - 1);
        JLabel lbTitle = new JLabel(menuName);
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:$Menu.label.font; foreground:$Menu.title.foreground");
        return lbTitle;
    }

    public boolean isMenuFull() {
        return menuFull;
    }

    public void setMenuFull(boolean menuFull) {
        this.menuFull = menuFull;
        if (menuFull) {
            header.setHorizontalAlignment(getComponentOrientation().isLeftToRight() ? JLabel.LEFT : JLabel.RIGHT);
            header.setText("Menu");
        } else {
            header.setText("");
            header.setHorizontalAlignment(JLabel.CENTER);
        }
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).setFull(menuFull);
            }
        }
        lightDarkMode.setMenuFull(menuFull);
        toolBarAccentColor.setMenuFull(menuFull);
    }

    public int getMenuMaxWidth() {
        return menuMaxWidth;
    }
    public int getMenuMinWidth() {
        return menuMinWidth;
    }
    public boolean isHideMenuTitleOnMinimum() {
        return hideMenuTitleOnMinimum;
    }
    public int getMenuTitleLeftInset() {
        return menuTitleLeftInset;
    }
    public int getMenuTitleVgap() {
        return menuTitleVgap;
    }

    public void setSelectedMenu(int index, int subIndex) throws IOException {
        runEvent(index, subIndex);
    }

    public void hideMenuItem() {
        for (Component com : panelMenu.getComponents()) {
            if (com instanceof MenuItem) {
                ((MenuItem) com).hideMenuItem();
            }
        }
        revalidate();
    }

    public void addMenuEvent(MenuEvent event) {
        events.add(event);
    }

    protected void runEvent(int index, int subIndex) throws IOException {
        MenuAction menuAction = new MenuAction();
        for (MenuEvent event : events) {
            event.menuSelected(index, subIndex, menuAction);
        }
        if(!menuAction.isCancel()) {

            setSelected(index, subIndex);
        }
    }

    protected void setSelected(int index, int subIndex) {
        int size = panelMenu.getComponentCount();
        for (int i = 0; i < size; i++) {
            Component com = panelMenu.getComponent(i);
            if (com instanceof MenuItem) {
                MenuItem item = (MenuItem) com;
                if (item.getMenuIndex() == index) {
                    item.setSelectedIndex(subIndex);
                } else {
                    item.setSelectedIndex(-1);
                }
            }
        }
    }

    private class MenuLayout implements LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {}
        @Override
        public void removeLayoutComponent(Component comp) {}
        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(5,5);
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
            int gap = UIScale.scale(5);
            int sheaderFullHgap = UIScale.scale(headerFullHgap);
            int width = parent.getWidth() - (insets.left + insets.right);
            int height = parent.getHeight() - (insets.top + insets.bottom);

            int iconWidth = width;
            int iconHeight = header.getPreferredSize().height;
            int hgap = menuFull ? sheaderFullHgap : 0;

            header.setBounds(x + hgap, y, iconWidth - (hgap * 2), iconHeight);

            int ldgap = UIScale.scale(10);
            int ldWidth = width - ldgap * 2;
            int ldHeight = lightDarkMode.getPreferredSize().height;
            int accentColorHeight = toolBarAccentColor.isVisible()
                    ? toolBarAccentColor.getPreferredSize().height + gap
                    : 0;

            int menux = x;
            int menuy = y + iconHeight + gap;
            int menuWidth = width;
            int menuHeight = height - (iconHeight + gap) - (ldHeight + ldgap * 2) - accentColorHeight;
            scroll.setBounds(menux, menuy, menuWidth, menuHeight);

            int ldx = x + ldgap;
            int ldy = y + height - ldHeight - ldgap - accentColorHeight;
            lightDarkMode.setBounds(ldx, ldy, ldWidth, ldHeight);

            if (toolBarAccentColor.isVisible()) {
                int tbheight = toolBarAccentColor.getPreferredSize().height;
                int tbwidth = Math.min(toolBarAccentColor.getPreferredSize().width, ldWidth);
                int tby = y + height - tbheight - ldgap;
                int tbx = ldx + ((ldWidth - tbwidth) / 2);
                toolBarAccentColor.setBounds(tbx, tby, tbwidth, tbheight);
            }
        }
    }
}
