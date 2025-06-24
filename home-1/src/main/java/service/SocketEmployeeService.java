// src/client/service/SocketEmployeeService.java
package service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import clientGui.RpcConnection;
import domain.Employee;

import java.io.IOException;
import java.util.List;

/**
 * EmployeeService implementation that routes all calls over TCP→JSON‐RPC
 * to the server listening on localhost:5000.
 */
public class    SocketEmployeeService implements EmployeeService {
    private static final ObjectMapper M = new ObjectMapper();
    static {
        try {
            RpcConnection.init("localhost", 5000);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to connect to RPC server on port 5000", ex);
        }
    }

    @Override
    public Employee createEmployee(int id, String name) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id",   id)
                .put("name", name);
        JsonNode r  = RpcConnection.call("employee", "create", p);
        return M.treeToValue(r, Employee.class);
    }

    @Override
    public Employee findById(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r   = RpcConnection.call("employee", "findById", p);
        return r.isNull() ? null : M.treeToValue(r, Employee.class);
    }

    @Override
    public List<Employee> findAll() throws IOException {
        JsonNode r = RpcConnection.call("employee", "findAll", null);
        return M.readerForListOf(Employee.class).readValue(r);
    }

    @Override
    public Employee updateEmployee(int id, String newName) throws IOException {
        ObjectNode p = M.createObjectNode()
                .put("id",   id)
                .put("name", newName);
        JsonNode r  = RpcConnection.call("employee", "update", p);
        return M.treeToValue(r, Employee.class);
    }

    @Override
    public Employee deleteEmployee(int id) throws IOException {
        ObjectNode p = M.createObjectNode().put("id", id);
        JsonNode r   = RpcConnection.call("employee", "delete", p);
        return M.treeToValue(r, Employee.class);
    }

    @Override
    public int getNextEmployeeId() throws IOException {
        // compute locally from findAll()
        List<Employee> all = findAll();
        return all.stream()
                .mapToInt(Employee::getIdEmployee)
                .max()
                .orElse(0) + 1;
    }
}
