package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import clientGui.RpcConnection;
import domain.User;

import java.io.IOException;
import java.util.List;

/**
 * UserService implementation that routes all calls over TCP→JSON‐RPC
 * to the server listening on localhost:5000.
 */
public class SocketUserService implements UserService {
    private static final ObjectMapper M = new ObjectMapper();
    static {
        try {
            RpcConnection.init("localhost", 5000);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to connect to RPC server on port 5000", ex);
        }
    }

    @Override
    public User createUser(int id, String name, String password) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id", id)
                .put("username", name)
                .put("password", password);
        JsonNode r = RpcConnection.call("user", "create", p);
        return M.treeToValue(r, User.class);
    }

    @Override
    public User findById(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r = RpcConnection.call("user", "findById", p);
        return r.isNull() ? null : M.treeToValue(r, User.class);
    }

    @Override
    public List<User> findAll() throws IOException {
        JsonNode r = RpcConnection.call("user", "findAll", null);
        return M.readerForListOf(User.class).readValue(r);
    }

    @Override
    public User updateUser(int id, String newName, String newPassword) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id", id)
                .put("username", newName)
                .put("password", newPassword);
        JsonNode r = RpcConnection.call("user", "update", p);
        return M.treeToValue(r, User.class);
    }

    @Override
    public User deleteUser(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r = RpcConnection.call("user", "delete", p);
        return M.treeToValue(r, User.class);
    }
}
