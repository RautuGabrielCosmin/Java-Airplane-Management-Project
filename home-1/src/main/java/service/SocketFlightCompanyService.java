package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import clientGui.RpcConnection;
import domain.FlightCompany;

import java.io.IOException;
import java.util.List;

/**
 * FlightCompanyService implementation that routes all calls over TCP→JSON‐RPC
 * to the server listening on localhost:5000.
 */
public class SocketFlightCompanyService implements FlightCompanyService {
    private static final ObjectMapper M = new ObjectMapper();
    static {
        try {
            RpcConnection.init("localhost", 5000);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to connect to RPC server on port 5000", ex);
        }
    }

    @Override
    public FlightCompany createFlightCompany(int id, String name, String location) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id", id)
                .put("name", name)
                .put("location", location);
        JsonNode r = RpcConnection.call("company", "create", p);
        return M.treeToValue(r, FlightCompany.class);
    }

    @Override
    public FlightCompany updateFlightCompany(int id, String newName, String newLocation) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id", id)
                .put("name", newName)
                .put("location", newLocation);
        JsonNode r = RpcConnection.call("company", "update", p);
        return M.treeToValue(r, FlightCompany.class);
    }

    @Override
    public FlightCompany deleteFlightCompany(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r = RpcConnection.call("company", "delete", p);
        return M.treeToValue(r, FlightCompany.class);
    }

    @Override
    public List<FlightCompany> findAll() throws IOException {
        JsonNode r = RpcConnection.call("company", "findAll", null);
        return M.readerForListOf(FlightCompany.class).readValue(r);
    }

    @Override
    public FlightCompany findById(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r = RpcConnection.call("company", "findById", p);
        return r.isNull() ? null : M.treeToValue(r, FlightCompany.class);
    }
}
