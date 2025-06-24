package gui.menu;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import gui.form.EmployeeForm;
import gui.form.FlightCompaniesForm1;
import gui.form.FlightsForm1;
import gui.form.TicketsForm1;
import gui.form.TouristsForm1;
import gui.form.TravelAgenciesForm1;
import gui.form.UsersForm1;
import gui.form.PurchaseForm1;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import org.example.App;

public class MainForm extends JLayeredPane {

    public MainForm() {
        init();
    }

    private void init() {
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setLayout(new MainFormLayout());
        menu = new Menu();
        panelBody = new JPanel(new BorderLayout());

        menuButton = new JButton("Menu Toggle");
        menuButton.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:$Menu.button.background;"
                + "arc:999;"
                + "focusWidth:0;"
                + "borderWidth:0");

        menuButton.addActionListener((ActionEvent e) -> {
            setMenuFull(!menu.isMenuFull());
        });
        initMenuEvent();
        setLayer(menuButton, JLayeredPane.POPUP_LAYER);
        add(menuButton);
        add(menu);
        add(panelBody);
    }

    private void initMenuEvent() {
        menu.addMenuEvent((int index, int subIndex, MenuAction action) -> {
            // Application.mainForm.showForm(new DefaultForm("Form : " + index + " " + subIndex));
            if (index == 1) {
                App.showForm(new PurchaseForm1());
            } else if (index == 0) {
                if (subIndex == 1) {
                    App.showForm(new EmployeeForm());
                } else if (subIndex == 2) {
                    App.showForm(new FlightsForm1());
                }else if(subIndex == 3){
                    App.showForm(new FlightCompaniesForm1());
                }else if(subIndex == 4){
                    App.showForm(new TicketsForm1());
                }
                else if(subIndex == 5){
                    App.showForm(new TouristsForm1());
                }
                else if(subIndex == 6){
                    App.showForm(new TravelAgenciesForm1());
                }
                else if(subIndex == 7){
                    App.showForm(new UsersForm1());
                }
                else {
                    action.cancel();
                }
            } else if(index == 2) {  // the last item is "Logout"
                Window parentWindow = SwingUtilities.getWindowAncestor(this);
                int confirm = JOptionPane.showConfirmDialog(
                        parentWindow,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    // yes => do actual logout
                    App.logout();
                } else {
                    // no => do not select "Logout"
                    action.cancel();
                    return;
                }
            } else {
                action.cancel();
            }
        });
    }

    private void setMenuFull(boolean full) {
        menu.setMenuFull(full);
        revalidate();
        repaint();
    }

    public void hideMenu() {
        menu.hideMenuItem();
    }

    public void showForm(Component component) {
        panelBody.removeAll();
        panelBody.add(component, BorderLayout.CENTER);
        panelBody.repaint();
        panelBody.revalidate();
    }

    public void setSelectedMenu(int index, int subIndex) throws IOException {
        menu.setSelectedMenu(index, subIndex);
    }

    private Menu menu;
    private JPanel panelBody;
    private JButton menuButton;

    private class MainFormLayout implements LayoutManager {
        @Override
        public void addLayoutComponent(String name, Component comp) {}
        @Override
        public void removeLayoutComponent(Component comp) {}
        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(900, 600);
        }
        @Override
        public Dimension minimumLayoutSize(Container parent) {
            return new Dimension(300, 200);
        }
        @Override
        public void layoutContainer(Container parent) {
            Insets insets = UIScale.scale(parent.getInsets());
            int x = insets.left;
            int y = insets.top;
            int width = parent.getWidth() - (insets.left + insets.right);
            int height = parent.getHeight() - (insets.top + insets.bottom);

            boolean ltr = parent.getComponentOrientation().isLeftToRight();
            int menuWidth = UIScale.scale(menu.isMenuFull() ? menu.getMenuMaxWidth() : menu.getMenuMinWidth());
            int menuX = ltr ? x : (x + width - menuWidth);
            menu.setBounds(menuX, y, menuWidth, height);

            int menuButtonWidth = menuButton.getPreferredSize().width;
            int menuButtonHeight = menuButton.getPreferredSize().height;
            int menubX;
            if (ltr) {
                menubX = menuX + menuWidth - (int)(menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.3f));
            } else {
                menubX = menuX - (int)(menuButtonWidth * (menu.isMenuFull() ? 0.5f : 0.7f));
            }
            menuButton.setBounds(menubX, UIScale.scale(30), menuButtonWidth, menuButtonHeight);

            int gap = UIScale.scale(5);
            int bodyWidth = width - menuWidth - gap;
            int bodyHeight = height;
            int bodyx = ltr ? (x + menuWidth + gap) : x;
            int bodyy = y;
            panelBody.setBounds(bodyx, bodyy, bodyWidth, bodyHeight);
        }
    }
}