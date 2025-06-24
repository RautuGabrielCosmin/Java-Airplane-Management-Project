package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Main server: listens on port 5000, accepts clients,
 * hands each off to a ClientHandler, and keeps a live list
 * so that each handler can broadcast to all others.
 */
public class ServerMain {
    private static final int PORT = 5000;

    /** Thread-safe list of all active client handlers */
    public static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

    /** Worker pool for handling each client connection */
    private static final ThreadPoolExecutor pool =
            (ThreadPoolExecutor) Executors.newCachedThreadPool();

    /** Underlying ServerSocket (null until started) */
    private static ServerSocket serverSocket;

    /**
     * Launches the socket server on PORT in a new daemon thread.
     * Safe to call multiple times; only first call actually starts it.
     */
    public static synchronized void startServer() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            // already running
            return;
        }
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to bind port " + PORT, ex);
        }

        Thread acceptThread = new Thread(() -> {
            try {
                while (!serverSocket.isClosed()) {
                    Socket clientSock = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(clientSock);
                    clients.add(handler);
                    pool.execute(handler);
                }
            } catch (IOException e) {
                if (!serverSocket.isClosed()) {
                    e.printStackTrace();
                }
            }
        }, "Server-Accept-Thread");
        acceptThread.setDaemon(true);
        acceptThread.start();
    }

    /**
     * Stops the server (for completeness; not strictly required).
     */
    public static synchronized void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server stopped");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverSocket = null;
        }
    }

    /**
     * Broadcasts a JSON‐RPC message to every connected client.
     * Dead connections are removed automatically.
     */
    public static void broadcast(String message) {
        for (ClientHandler c : clients) {
            if (!c.send(message)) {
                clients.remove(c);
            }
        }
    }

    /**
     * If you still want a command‐line entrypoint, just call startServer().
     */
    public static void main(String[] args) {
        startServer();
        // keep main thread alive, or simply exit if you want only the accept thread:
        try {
            Thread.currentThread().join();
        } catch (InterruptedException ignored) { }
    }
}
