// src/client/clientGui/RpcConnection.java
package clientGui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Simple JSON‐RPC over TCP helper.
 *
 * Initialize once with init(), then call service RPCs via call().
 */
public class RpcConnection {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static Socket socket;
    private static BufferedReader in;
    private static BufferedWriter out;

    /**
     * Opens a single TCP connection to the RPC server.
     * Must be called before any call().
     *
     * @param host server hostname (e.g. "localhost")
     * @param port server port (e.g. 5000)
     * @throws IOException on network error
     */
    public static synchronized void init(String host, int port) throws IOException {
        if (socket != null && socket.isConnected() && !socket.isClosed()) {
            return; // already initialized
        }
        socket = new Socket(host, port);
        in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    /**
     * Sends one JSON‐RPC request and waits for one response.
     *
     * @param service  the service name (e.g. "employee")
     * @param action   the action name (e.g. "create")
     * @param params   an ObjectNode of parameters, or null if none
     * @return the JsonNode under "result"
     * @throws IOException on network or parsing error
     */
    public static synchronized JsonNode call(String service, String action, ObjectNode params)
            throws IOException {
        if (socket == null || socket.isClosed()) {
            throw new IllegalStateException("RpcConnection not initialized");
        }

        // Build request
        ObjectNode req = MAPPER.createObjectNode()
                .put("requestId", Long.toString(System.nanoTime()))
                .put("service", service)
                .put("action",  action);
        if (params != null) {
            req.set("params", params);
        }

        // Send it
        String requestLine = req.toString();
        out.write(requestLine);
        out.write("\n");
        out.flush();

        // Read response
        String responseLine = in.readLine();
        if (responseLine == null) {
            throw new IOException("Connection closed by server");
        }
        JsonNode resp = MAPPER.readTree(responseLine);

        // Error?
        if (resp.has("status") && "error".equals(resp.get("status").asText())) {
            String msg = resp.has("message") ? resp.get("message").asText() : "Unknown error";
            throw new RuntimeException("RPC error: " + msg);
        }

        return resp.get("result");
    }



    /**
     * Closes the underlying socket.  Future calls to call() will fail.
     */
    public static synchronized void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) { }
        socket = null;
        in     = null;
        out    = null;
    }

    // at the bottom of your existing RpcConnection.java
    public static String readLine() throws IOException {
        return in.readLine();
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

}
