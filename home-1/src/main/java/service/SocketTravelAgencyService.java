package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import clientGui.RpcConnection;
import domain.TravelAgency;

import java.io.IOException;
import java.util.List;

/**
 * TravelAgencyService implementation that routes all calls over TCP→JSON‐RPC
 * to the server listening on localhost:5000.
 */
public class SocketTravelAgencyService implements TravelAgencyService {
    private static final ObjectMapper M = new ObjectMapper();
    static {
        try {
            RpcConnection.init("localhost", 5000);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to connect to RPC server on port 5000", ex);
        }
    }

    @Override
    public TravelAgency createTravelAgency(int id,
                                           String name,
                                           int numberOfEmployees,
                                           String flightsBooked) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id", id)
                .put("name", name)
                .put("employees", numberOfEmployees)
                .put("flightsBooked", flightsBooked);
        JsonNode r = RpcConnection.call("agency", "create", p);
        return M.treeToValue(r, TravelAgency.class);
    }

    @Override
    public TravelAgency findById(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r = RpcConnection.call("agency", "findById", p);
        return r.isNull() ? null : M.treeToValue(r, TravelAgency.class);
    }

    @Override
    public List<TravelAgency> findAll() throws IOException {
        JsonNode r = RpcConnection.call("agency", "findAll", null);
        return M.readerForListOf(TravelAgency.class).readValue(r);
    }

    @Override
    public TravelAgency updateTravelAgency(int id,
                                           String newName,
                                           int newNumberOfEmployees,
                                           String newFlightsBooked) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id", id)
                .put("name", newName)
                .put("employees", newNumberOfEmployees)
                .put("flightsBooked", newFlightsBooked);
        JsonNode r = RpcConnection.call("agency", "update", p);
        return M.treeToValue(r, TravelAgency.class);
    }

    @Override
    public TravelAgency deleteTravelAgency(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r = RpcConnection.call("agency", "delete", p);
        return M.treeToValue(r, TravelAgency.class);
    }
}
