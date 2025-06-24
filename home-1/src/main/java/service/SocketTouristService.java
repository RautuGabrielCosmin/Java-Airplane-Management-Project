package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import clientGui.RpcConnection;
import domain.Tourist;

import java.io.IOException;
import java.util.List;

/**
 * TouristService implementation that routes all calls over TCP→JSON‐RPC
 * to the server listening on localhost:5000.
 */
public class SocketTouristService implements TouristService {
    private static final ObjectMapper M = new ObjectMapper();
    static {
        try {
            RpcConnection.init("localhost", 5000);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to connect to RPC server on port 5000", ex);
        }
    }

    @Override
    public Tourist createTourist(int id, String name) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id", id)
                .put("name", name);
        JsonNode r = RpcConnection.call("tourist", "create", p);
        return M.treeToValue(r, Tourist.class);
    }

    @Override
    public Tourist findById(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r = RpcConnection.call("tourist", "findById", p);
        return r.isNull() ? null : M.treeToValue(r, Tourist.class);
    }

    @Override
    public List<Tourist> findAll() throws IOException {
        JsonNode r = RpcConnection.call("tourist", "findAll", null);
        return M.readerForListOf(Tourist.class).readValue(r);
    }

    @Override
    public Tourist updateTourist(int id, String newName) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id", id)
                .put("name", newName);
        JsonNode r = RpcConnection.call("tourist", "update", p);
        return M.treeToValue(r, Tourist.class);
    }

    @Override
    public Tourist deleteTourist(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r = RpcConnection.call("tourist", "delete", p);
        return M.treeToValue(r, Tourist.class);
    }
}
