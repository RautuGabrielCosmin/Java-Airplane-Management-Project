package serverGui;

import com.formdev.flatlaf.FlatDarkLaf;
import server.ServerMain;

import domain.Employee;
import domain.Flight;
import domain.FlightCompany;
import domain.Ticket;
import domain.Tourist;
import domain.TravelAgency;
import domain.User;

import repository.MemoryEmployee;
import repository.MemoryFlight;
import repository.MemoryFlightCompany;
import repository.MemoryTicket;
import repository.MemoryTourist;
import repository.MemoryTravelAgency;
import repository.MemoryUser;

import service.EmployeeService;
import service.FlightService;
import service.FlightCompanyService;
import service.TicketService;
import service.TouristService;
import service.TravelAgencyService;
import service.UserService;

import service.MemoryEmployeeService;
import service.MemoryFlightService;
import service.MemoryFlightCompanyService;
import service.MemoryTicketService;
import service.MemoryTouristService;
import service.MemoryTravelAgencyService;
import service.MemoryUserService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

/**
 * Swing dashboard + launches the Socket RPC server (port 5000).
 * Displays a live count of connected clients and catches IOExceptions
 * from the socket-backed services so the UI compiles without errors.
 */
public class ServerDashboardFrame extends JFrame
        implements ActionListener, WindowFocusListener {

    private final JComboBox<String> tableSelector = new JComboBox<>(
            new String[]{"Employees", "Flights", "Flight Companies",
                    "Users", "Tickets", "Tourists", "Travel Agencies"});
    private final JTable table = new JTable();
    private final JLabel clientCountLabel = new JLabel("Clients: 0");

    // still using the in-memory services for dashboard view
    private final EmployeeService      empSrv = new MemoryEmployeeService(new MemoryEmployee());
    private final FlightService        fltSrv = new MemoryFlightService(new MemoryFlight());
    private final FlightCompanyService cmpSrv = new MemoryFlightCompanyService(new MemoryFlightCompany());
    private final UserService          usrSrv = new MemoryUserService(new MemoryUser());
    private final TicketService        tckSrv = new MemoryTicketService(new MemoryTicket());
    private final TouristService       trsSrv = new MemoryTouristService(new MemoryTourist());
    private final TravelAgencyService  agySrv = new MemoryTravelAgencyService(new MemoryTravelAgency());

    public ServerDashboardFrame() {
        super("Server-Side Dashboard");
        FlatDarkLaf.setup();

        // 1) start our RPC server (daemon thread)
        ServerMain.startServer();

        // 2) top panel
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
        top.add(new JLabel("Show table:"));
        top.add(tableSelector);
        top.add(Box.createHorizontalStrut(30));

        clientCountLabel.setFont(clientCountLabel.getFont().deriveFont(Font.BOLD));
        clientCountLabel.setForeground(Color.WHITE);
        top.add(clientCountLabel);

        // 3) table center
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));

        setLayout(new BorderLayout());
        add(top,    BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // 4) wire events
        tableSelector.addActionListener(this);
        addWindowFocusListener(this);

        // 5) initial load
        tableSelector.setSelectedIndex(0);
        reloadTable();

        // 6) live client count
        new Timer(1000, e ->
                clientCountLabel.setText("Clients: " + ServerMain.clients.size())
        ).start();

        // 7) window settings
        setMinimumSize(new Dimension(900,600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter(){
            @Override public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });
        setVisible(true);
    }

    // catch-all helper to center first column
    private static void applyModel(JTable table, DefaultTableModel m, int... widths) {
        table.setModel(m);
        for(int i=0; i<widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        DefaultTableCellRenderer c = new DefaultTableCellRenderer();
        c.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(c);
    }

    @Override public void actionPerformed(ActionEvent e) { reloadTable(); }
    @Override public void windowGainedFocus(WindowEvent e) { reloadTable(); }
    @Override public void windowLostFocus(WindowEvent e)  { /* no-op */ }

    private void reloadTable() {
        String sel = (String) tableSelector.getSelectedItem();
        if (sel == null) return;

        switch (sel) {
            case "Employees"        -> loadEmployees();
            case "Flights"          -> loadFlights();
            case "Flight Companies" -> loadFlightCompanies();
            case "Users"            -> loadUsers();
            case "Tickets"          -> loadTickets();
            case "Tourists"         -> loadTourists();
            case "Travel Agencies"  -> loadTravelAgencies();
        }
    }

    private void loadEmployees() {
        DefaultTableModel m = new DefaultTableModel(new Object[]{"ID","Name"}, 0);
        try {
            List<Employee> all = empSrv.findAll();  // throws IOException
            for (Employee e : all) {
                m.addRow(new Object[]{ e.getIdEmployee(), e.getName() });
            }
        } catch (IOException ex) {
            showError(ex);
        }
        applyModel(table, m, 60,240);
    }

    private void loadFlights() {
        DefaultTableModel m = new DefaultTableModel(new Object[]{"ID","From","To"}, 0);
        try {
            List<Flight> all = fltSrv.findAll();      // now may throw IOException
            for (Flight f : all) {
                m.addRow(new Object[]{
                        f.getId(),
                        f.getLocationDeparture(),
                        f.getLocationArrival()
                });
            }
        } catch (IOException ex) {
            showError(ex);
        }
        applyModel(table, m, 60,160,160);
    }

    private void loadFlightCompanies() {
        DefaultTableModel m = new DefaultTableModel(new Object[]{"ID","Name"}, 0);
        try {
            List<FlightCompany> all = cmpSrv.findAll();  // may throw IOException
            for (FlightCompany c : all) {
                m.addRow(new Object[]{ c.getIdFlight(), c.getName() });
            }
        } catch (IOException ex) {
            showError(ex);
        }
        applyModel(table, m, 60,200);
    }

    private void loadUsers() {
        DefaultTableModel m = new DefaultTableModel(new Object[]{"ID","Username"}, 0);
        try {
            List<User> all = usrSrv.findAll();  // may throw IOException
            for (User u : all) {
                m.addRow(new Object[]{ u.getId(), u.getUsername() });
            }
        } catch (IOException ex) {
            showError(ex);
        }
        applyModel(table, m, 60,200);
    }

    private void loadTickets() {
        DefaultTableModel m = new DefaultTableModel(new Object[]{"ID","Customer","Seat"}, 0);
        try {
            List<Ticket> all = tckSrv.findAllTickets();  // may throw IOException
            for (Ticket t : all) {
                m.addRow(new Object[]{
                        t.getId(),
                        t.getCustomerName(),
                        t.getSeatType()
                });
            }
        } catch (IOException ex) {
            showError(ex);
        }
        applyModel(table, m, 60,200,80);
    }

    private void loadTourists() {
        DefaultTableModel m = new DefaultTableModel(new Object[]{"ID","Name"}, 0);
        try {
            List<Tourist> all = trsSrv.findAll();  // may throw IOException
            for (Tourist t : all) {
                m.addRow(new Object[]{ t.getIdTourist(), t.getName() });
            }
        } catch (IOException ex) {
            showError(ex);
        }
        applyModel(table, m, 60,200);
    }

    private void loadTravelAgencies() {
        DefaultTableModel m = new DefaultTableModel(new Object[]{"ID","Name","Employees"}, 0);
        try {
            List<TravelAgency> all = agySrv.findAll();  // may throw IOException
            for (TravelAgency a : all) {
                m.addRow(new Object[]{
                        a.getIdTravelAgency(),
                        a.getName(),
                        a.getNumberOfEmployees()
                });
            }
        } catch (IOException ex) {
            showError(ex);
        }
        applyModel(table, m, 60,180,100);
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ServerDashboardFrame::new);
    }
}
