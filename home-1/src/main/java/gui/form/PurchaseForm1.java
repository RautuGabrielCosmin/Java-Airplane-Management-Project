package gui.form;

import com.formdev.flatlaf.FlatClientProperties;
import domain.Ticket;
import gui.table.ButtonAction;
import net.miginfocom.swing.MigLayout;
import service.TicketService;
import service.SocketTicketService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Purchase form wired over JSON‐RPC on port 5000.
 * When you create a ticket, it goes through SocketTicketService,
 * so the server will persist it and broadcast to all clients.
 */
public class PurchaseForm1 extends JPanel {

    /* ─── RPC service ─────────────────────────────────────────────────────── */
    private final TicketService ticketService = new SocketTicketService();

    /* ─── UI components ───────────────────────────────────────────────────── */
    private JLabel      lbTitle1;
    private JLabel      lbTitle2;
    private JTextField  txtName;
    private JComboBox<String> cbDeparture;
    private JComboBox<String> cbArrival;
    private JTextField  txtDateOfDeparture;
    private JComboBox<String> cbSeatType;
    private JButton     btnSave;

    /** optional parent table to refresh after purchase */
    private final TicketsForm1 parentTicketsForm;

    public PurchaseForm1() {
        this(null);
    }
    public PurchaseForm1(TicketsForm1 parent) {
        this.parentTicketsForm = parent;
        initComponents();
    }

    private void initComponents() {
        setLayout(new MigLayout("wrap 2, insets 30, fillx",
                "[right]10[grow,fill]", ""));

        putClientProperty(FlatClientProperties.STYLE,
                "arc:20;"
                        + "[light]background:darken(@background,3%);"
                        + "[dark]background:lighten(@background,3%)");

        // Personal details header
        lbTitle1 = new JLabel("Personal Details");
        lbTitle1.putClientProperty(FlatClientProperties.STYLE, "font:$h2.font");
        add(lbTitle1, "span 2, gapbottom 10");

        // Name
        JLabel lbName = new JLabel("Name:");
        lbName.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
        add(lbName);

        txtName = new JTextField();
        txtName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter your name");
        txtName.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
        add(txtName, "growx, wrap");

        // Separator
        add(new JSeparator(), "span 2, growx, gaptop 15, gapbottom 15");

        // Flight details header
        lbTitle2 = new JLabel("Flight Details");
        lbTitle2.putClientProperty(FlatClientProperties.STYLE, "font:$h2.font");
        add(lbTitle2, "span 2, gapbottom 10");

        // Departure
        JLabel lbDeparture = new JLabel("Departure:");
        lbDeparture.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
        add(lbDeparture);

        cbDeparture = new JComboBox<>(cities());
        cbDeparture.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
        add(cbDeparture, "growx, wrap");

        // Arrival
        JLabel lbArrival = new JLabel("Arrival:");
        lbArrival.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
        add(lbArrival);

        cbArrival = new JComboBox<>(cities());
        cbArrival.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
        add(cbArrival, "growx, wrap");

        // Date
        JLabel lbDate = new JLabel("Date of Departure:");
        lbDate.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
        add(lbDate);

        txtDateOfDeparture = new JTextField("2025-04-10");
        txtDateOfDeparture.putClientProperty(
                FlatClientProperties.PLACEHOLDER_TEXT, "eg. 2025-04-10");
        txtDateOfDeparture.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
        add(txtDateOfDeparture, "growx, wrap");

        // Seat type
        JLabel lbSeat = new JLabel("Seat Type:");
        lbSeat.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
        add(lbSeat);

        cbSeatType = new JComboBox<>(new String[]{"Economy","Business","First"});
        cbSeatType.putClientProperty(FlatClientProperties.STYLE, "font:$h4.font");
        add(cbSeatType, "growx, wrap");

        // Save button
        btnSave = new JButton("Purchase Ticket");
        btnSave.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:darken(@background,10%);"
                        + "[dark]background:lighten(@background,10%);"
                        + "borderWidth:0;focusWidth:0;innerFocusWidth:0;font:$h4.font");
        btnSave.addActionListener(this::handleSave);
        add(btnSave, "skip 1, align right, gaptop 15");
    }

    /** Some sample cities (shortened) */
    private static String[] cities() {
        return new String[]{
                "London","Boston","Dubai","Tokyo","New York","Chicago","Sydney",
                "Paris","Berlin","Amsterdam","Singapore","Hong Kong","Rome",
                "Oslo","Manchester","Los Angeles","Madrid","Istanbul","Beijing","Toronto"
        };
    }

    /** Called when user clicks “Purchase Ticket” */
    private void handleSave(ActionEvent e) {
        try {
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty.");
            }

            // Build the ticket parameters
            int    id          = ThreadLocalRandom.current().nextInt(1, 1_000_000);
            int    flightId    = ThreadLocalRandom.current().nextInt(1, 1000);
            int    touristId   = ThreadLocalRandom.current().nextInt(1, 1000);
            int    employeeId  = ThreadLocalRandom.current().nextInt(1, 1000);
            String seatType    = (String) cbSeatType.getSelectedItem();
            String seatNumber  = String.valueOf(ThreadLocalRandom.current().nextInt(1, 300));
            String custName    = name;

            // **RPC call** — this goes to the server on port 5000
            Ticket t = ticketService.createTicket(
                    id, flightId, touristId, employeeId,
                    seatType, seatNumber, custName
            );

            // Success feedback
            JOptionPane.showMessageDialog(this,
                    "Ticket purchased successfully:\n" + t,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // refresh parent table if present
            if (parentTicketsForm != null) {
                parentTicketsForm.reloadData();
            }

            // reset inputs
            txtName.setText("");
            txtDateOfDeparture.setText("2025-04-10");
            cbDeparture.setSelectedIndex(0);
            cbArrival.setSelectedIndex(0);
            cbSeatType.setSelectedIndex(0);

        } catch (IllegalArgumentException ia) {
            JOptionPane.showMessageDialog(this,
                    ia.getMessage(),
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(this,
                    "I/O error while purchasing ticket:\n" + ioe.getMessage(),
                    "Network Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Unexpected error:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
