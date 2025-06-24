package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import clientGui.RpcConnection;
import domain.Flight;

import java.io.IOException;
import java.util.List;

/**
 * FlightService implementation that routes all calls over TCP→JSON‐RPC
 * to the server listening on localhost:5000.
 */
public class SocketFlightService implements FlightService {
    private static final ObjectMapper M = new ObjectMapper();
    static {
        try {
            RpcConnection.init("localhost", 5000);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to connect to RPC server on port 5000", ex);
        }
    }

    @Override
    public Flight createFlight(int id,
                               String locationDeparture,
                               String locationArrival,
                               String dateDeparture,
                               String dateArrival,
                               int numberOfSeats,
                               int flightCompanyId) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id", id)
                .put("from", locationDeparture)
                .put("to", locationArrival)
                .put("dateDeparture", dateDeparture)
                .put("dateArrival", dateArrival)
                .put("seats", numberOfSeats)
                .put("companyId", flightCompanyId);
        JsonNode r = RpcConnection.call("flight", "create", p);
        return M.treeToValue(r, Flight.class);
    }

    @Override
    public Flight findById(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r = RpcConnection.call("flight", "findById", p);
        return r.isNull() ? null : M.treeToValue(r, Flight.class);
    }

    @Override
    public List<Flight> findAll() throws IOException {
        JsonNode r = RpcConnection.call("flight", "findAll", null);
        return M.readerForListOf(Flight.class).readValue(r);
    }

    @Override
    public Flight updateFlight(int id,
                               String newLocationDeparture,
                               String newLocationArrival,
                               String newDateDeparture,
                               String newDateArrival,
                               int newNumberOfSeats,
                               int newFlightCompanyId) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id", id)
                .put("from", newLocationDeparture)
                .put("to", newLocationArrival)
                .put("dateDeparture", newDateDeparture)
                .put("dateArrival", newDateArrival)
                .put("seats", newNumberOfSeats)
                .put("companyId", newFlightCompanyId);
        JsonNode r = RpcConnection.call("flight", "update", p);
        return M.treeToValue(r, Flight.class);
    }

    @Override
    public Flight deleteFlight(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r = RpcConnection.call("flight", "delete", p);
        return M.treeToValue(r, Flight.class);
    }
}
