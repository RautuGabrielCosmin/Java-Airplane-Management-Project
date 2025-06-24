package gui.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MenuItem extends JPanel {

    private final Menu menu;
    private final String[] menus;
    private final int menuIndex;
    private final List<MenuEvent> events;

    private boolean menuShow;
    private float animate;

    private final int menuItemHeight = 38;
    private final int subMenuItemHeight = 35;
    private final int subMenuLeftGap = 34;
    private final int firstGap = 5;
    private final int bottomGap = 5;

    private PopupSubmenu popup;

    public MenuItem(Menu menu, String[] menus, int menuIndex, List<MenuEvent> events) {
        this.menu = menu;
        this.menus = menus;
        this.menuIndex = menuIndex;
        this.events = events;
        init();
    }

    private void init() {
        setLayout(new MenuLayout());
        putClientProperty(FlatClientProperties.STYLE, "background:$Menu.background; foreground:$Menu.lineColor");

        for (int i = 0; i < menus.length; i++) {
            JButton btn = createButtonItem(menus[i]);
            if (i == 0) {
                // Root item
                btn.addActionListener((ActionEvent e) -> {
                    if (menus.length > 1) {
                        if (menu.isMenuFull()) {
                            MenuAnimation.animate(MenuItem.this, !menuShow);
                        } else {
                            popup.show(this, getWidth() + UIScale.scale(5), UIScale.scale(menuItemHeight) / 2);
                        }
                    } else {
                        try {
                            menu.runEvent(menuIndex, 0);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            } else {
                // Sub items
                final int subIndex = i;
                btn.addActionListener(e -> {
                    try {
                        menu.runEvent(menuIndex, subIndex);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
            }
            add(btn);
        }
        popup = new PopupSubmenu(getComponentOrientation(), menu, menuIndex, menus);
    }

    private JButton createButtonItem(String text) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);  // Force left‐aligned
        button.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.background;"
                + "foreground:$Menu.foreground;"
                + "selectedBackground:$Menu.button.selectedBackground;"
                + "selectedForeground:$Menu.button.selectedForeground;"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "arc:10;"
                + "iconTextGap:10;"
                + "margin:3,11,3,11");
        return button;
    }

    protected void setSelectedIndex(int index) {
        int size = getComponentCount();
        boolean selected = false;
        for (int i = 0; i < size; i++) {
            Component c = getComponent(i);
            if (c instanceof JButton) {
                ((JButton)c).setSelected(i == index);
                if (i == index) selected = true;
            }
        }
        // root item is selected if sub item is selected
        ((JButton)getComponent(0)).setSelected(selected);
        popup.setSelectedIndex(index);
    }

    public boolean isMenuShow() { return menuShow; }
    public void setMenuShow(boolean menuShow) { this.menuShow = menuShow; }

    public float getAnimate() { return animate; }
    public void setAnimate(float animate) { this.animate = animate; }

    public String[] getMenus() { return menus; }
    public int getMenuIndex() { return menuIndex; }

    public void hideMenuItem() {
        animate = 0f;
        menuShow = false;
    }

    public void setFull(boolean full) {
        if (full) {
            // show text
            for (int i = 0; i < getComponentCount(); i++) {
                Component c = getComponent(i);
                if (c instanceof JButton) {
                    ((JButton) c).setText(menus[i]);
                    ((JButton) c).setHorizontalAlignment(SwingConstants.LEFT);
                }
            }
        } else {
            // hide text
            for (Component c : getComponents()) {
                if (c instanceof JButton) {
                    ((JButton) c).setText("");
                    ((JButton) c).setHorizontalAlignment(SwingConstants.CENTER);
                }
            }
            animate = 0f;
            menuShow = false;
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // If multiple sub items, draw the arrow shape
        if (menus.length > 1) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setColor(FlatUIUtils.getUIColor("Menu.arrowColor", getForeground()));
            g2.setStroke(new BasicStroke(UIScale.scale(1f)));

            int smenuItemHeight = UIScale.scale(menuItemHeight);
            boolean ltr = getComponentOrientation().isLeftToRight();

            if (menu.isMenuFull()) {
                int arrowWidth = UIScale.scale(10);
                int arrowHeight = UIScale.scale(5);
                int ax = ltr ? (getWidth() - arrowWidth * 2) : arrowWidth;
                int ay = (smenuItemHeight - arrowHeight) / 2;
                Path2D p = new Path2D.Double();
                p.moveTo(0, animate * arrowHeight);
                p.lineTo(arrowWidth / 2.0, (1f - animate) * arrowHeight);
                p.lineTo(arrowWidth, animate * arrowHeight);
                g2.translate(ax, ay);
                g2.draw(p);
            } else {
                int arrowWidth = UIScale.scale(4);
                int arrowHeight = UIScale.scale(8);
                int ax = ltr ? (getWidth() - arrowWidth - UIScale.scale(3)) : UIScale.scale(3);
                int ay = (smenuItemHeight - arrowHeight) / 2;
                Path2D p = new Path2D.Double();
                if (ltr) {
                    p.moveTo(0, 0);
                    p.lineTo(arrowWidth, arrowHeight / 2.0);
                    p.lineTo(0, arrowHeight);
                } else {
                    p.moveTo(arrowWidth, 0);
                    p.lineTo(0, arrowHeight / 2.0);
                    p.lineTo(arrowWidth, arrowHeight);
                }
                g2.translate(ax, ay);
                g2.draw(p);
            }
            g2.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (animate > 0) {
            int ssubMenuItemHeight = UIScale.scale(subMenuItemHeight);
            int ssubMenuLeftGap = UIScale.scale(subMenuLeftGap);
            int smenuItemHeight = UIScale.scale(menuItemHeight);
            int sfirstGap = UIScale.scale(firstGap);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Path2D.Double p = new Path2D.Double();
            int last = getComponent(getComponentCount() - 1).getY() + (ssubMenuItemHeight / 2);
            boolean ltr = getComponentOrientation().isLeftToRight();
            int round = UIScale.scale(10);

            int x = ltr ? (ssubMenuLeftGap - round) : (getWidth() - (ssubMenuLeftGap - round));

            p.moveTo(x, smenuItemHeight + sfirstGap);
            p.lineTo(x, last - round);

            for (int i = 1; i < getComponentCount(); i++) {
                int com = getComponent(i).getY() + (ssubMenuItemHeight / 2);
                p.append(createCurve(round, x, com, ltr), false);
            }
            g2.setColor(getForeground());
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setStroke(new BasicStroke(UIScale.scale(1f)));
            g2.draw(p);
            g2.dispose();
        }
    }

    private Shape createCurve(int round, int x, int y, boolean ltr) {
        Path2D p2 = new Path2D.Double();
        p2.moveTo(x, y - round);
        p2.curveTo(x, y - round, x, y, x + (ltr ? round : -round), y);
        return p2;
    }

    private class MenuLayout implements LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {}
        @Override
        public void removeLayoutComponent(Component comp) {}
        @Override
        public Dimension preferredLayoutSize(Container parent) {
            Insets inset = parent.getInsets();
            int width = parent.getWidth();
            int height = inset.top + inset.bottom;
            int count = parent.getComponentCount();
            if (count > 0) {
                Component rootItem = parent.getComponent(0);
                if (rootItem.isVisible()) {
                    height += UIScale.scale(menuItemHeight);
                    int subMenuHeight = (count > 1 ? UIScale.scale(firstGap) + UIScale.scale(bottomGap) : 0);
                    for (int i = 1; i < count; i++) {
                        Component c = parent.getComponent(i);
                        if (c.isVisible()) {
                            subMenuHeight += UIScale.scale(subMenuItemHeight);
                        }
                    }
                    height += (subMenuHeight * animate);
                }
            }
            return new Dimension(width, height);
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
            int width = parent.getWidth() - (insets.left + insets.right);
            int count = parent.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component c = parent.getComponent(i);
                if (c.isVisible()) {
                    if (i == 0) {
                        int smenuItemHeight = UIScale.scale(menuItemHeight);
                        int sfirstGap = UIScale.scale(firstGap);
                        c.setBounds(x, y, width, smenuItemHeight);
                        y += smenuItemHeight + sfirstGap;
                    } else {
                        int ssubMenuLeftGap = UIScale.scale(subMenuLeftGap);
                        int ssubMenuItemHeight = UIScale.scale(subMenuItemHeight);
                        c.setBounds(x + ssubMenuLeftGap, y, width - ssubMenuLeftGap, ssubMenuItemHeight);
                        y += ssubMenuItemHeight;
                    }
                }
            }
        }
    }
}
