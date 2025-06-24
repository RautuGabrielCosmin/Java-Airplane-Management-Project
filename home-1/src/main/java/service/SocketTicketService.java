package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import clientGui.RpcConnection;
import domain.Ticket;

import java.io.IOException;
import java.util.List;

/**
 * TicketService implementation that routes all calls over TCP→JSON‐RPC
 * to the server listening on localhost:5000.
 */
public class SocketTicketService implements TicketService {
    private static final ObjectMapper M = new ObjectMapper();
    static {
        try {
            RpcConnection.init("localhost", 5000);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to connect to RPC server on port 5000", ex);
        }
    }

    @Override
    public Ticket createTicket(int id,
                               int flightId,
                               int touristId,
                               int employeeId,
                               String seatType,
                               String seatNumber,
                               String customerName) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id", id)
                .put("flightId", flightId)
                .put("touristId", touristId)
                .put("employeeId", employeeId)
                .put("seatType", seatType)
                .put("seatNumber", seatNumber)
                .put("customerName", customerName);
        JsonNode r = RpcConnection.call("ticket", "create", p);
        return M.treeToValue(r, Ticket.class);
    }

    @Override
    public Ticket findById(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r = RpcConnection.call("ticket", "findById", p);
        return r.isNull() ? null : M.treeToValue(r, Ticket.class);
    }

    @Override
    public List<Ticket> findAllTickets() throws IOException {
        JsonNode r = RpcConnection.call("ticket", "findAll", null);
        return M.readerForListOf(Ticket.class).readValue(r);
    }

    @Override
    public Ticket updateTicket(int id,
                               int newFlightId,
                               String newSeatType,
                               String newSeatNumber,
                               String newCustomerName) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id", id)
                .put("flightId", newFlightId)
                .put("seatType", newSeatType)
                .put("seatNumber", newSeatNumber)
                .put("customerName", newCustomerName);
        JsonNode r = RpcConnection.call("ticket", "update", p);
        return M.treeToValue(r, Ticket.class);
    }

    @Override
    public Ticket deleteTicket(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r = RpcConnection.call("ticket", "delete", p);
        return M.treeToValue(r, Ticket.class);
    }
}
