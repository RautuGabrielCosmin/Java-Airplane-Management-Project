package server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import service.EmployeeService;
import service.FlightService;
import service.FlightCompanyService;
import service.UserService;
import service.TicketService;
import service.TouristService;
import service.TravelAgencyService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Handles one client connection.  Reads JSON‐RPC requests, dispatches
 * via your service layer, returns the result, then broadcasts that same
 * JSON‐RPC response to **all** connected clients (so everyone stays in sync).
 */
public class ClientHandler implements Runnable {
    private static final ObjectMapper M = new ObjectMapper();

    private final Socket socket;
    private final BufferedReader in;
    private final BufferedWriter out;

    // explicitly typed service references from AppContext
    private final EmployeeService       empSrv = AppContext.EMP_SRV;
    private final FlightService         fltSrv = AppContext.FLT_SRV;
    private final FlightCompanyService  cmpSrv = AppContext.CMP_SRV;
    private final UserService           usrSrv = AppContext.USR_SRV;
    private final TicketService         tckSrv = AppContext.TCK_SRV;
    private final TouristService        trsSrv = AppContext.TRS_SRV;
    private final TravelAgencyService   agySrv = AppContext.AGY_SRV;

    public ClientHandler(Socket sock) throws IOException {
        this.socket = sock;
        this.in     = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        this.out    = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                ObjectNode req       = (ObjectNode) M.readTree(line);
                String     requestId = req.path("requestId").asText("");
                String     svc       = req.path("service").asText();
                String     action    = req.path("action").asText();
                JsonNode   params    = req.path("params");

                ObjectNode resp = M.createObjectNode()
                        .put("requestId", requestId);

                try {
                    JsonNode result = dispatch(svc, action, params);
                    resp.put("status", "ok")
                            .set("result", result);
                    String full = resp.toString();

                    // reply to this client
                    send(full);
                    // AND broadcast to everyone else
                    ServerMain.broadcast(full);

                } catch (Exception ex) {
                    resp.put("status", "error")
                            .put("message", ex.getMessage());
                    send(resp.toString());
                }
            }
        } catch (IOException ignore) {
            // client disconnected
        } finally {
            close();
        }
    }

    /**
     * Send one JSON‐RPC line to this client.
     * @return false if socket is dead
     */
    public boolean send(String message) {
        try {
            out.write(message);
            out.write("\n");
            out.flush();
            return true;
        } catch (IOException e) {
            close();
            return false;
        }
    }

    /**
     * Close connection and remove from client list so dashboard count decrements.
     */
    private void close() {
        try {
            socket.close();
        } catch (IOException ignored) {}

        // unregister from the live clients list
        ServerMain.clients.remove(this);
        AppContext.unregister(this);
    }

    /**
     * Exactly mirrors your old RpcDispatcher logic—one big nested switch
     * turning {service,action,params} into a JsonNode result.
     */
    private JsonNode dispatch(String svc, String action, JsonNode p) throws Exception {
        return switch (svc) {
            case "employee" -> switch (action) {
                case "findAll"   -> M.valueToTree(empSrv.findAll());
                case "findById"  -> M.valueToTree(empSrv.findById(p.get("id").asInt()));
                case "create"    -> M.valueToTree(empSrv.createEmployee(
                        p.get("id").asInt(), p.get("name").asText()));
                case "update"    -> M.valueToTree(empSrv.updateEmployee(
                        p.get("id").asInt(), p.get("name").asText()));
                case "delete"    -> M.valueToTree(empSrv.deleteEmployee(p.get("id").asInt()));
                default          -> throw new IllegalArgumentException("Unknown employee action: " + action);
            };
            case "flight" -> switch (action) {
                case "findAll"   -> M.valueToTree(fltSrv.findAll());
                case "findById"  -> M.valueToTree(fltSrv.findById(p.get("id").asInt()));
                case "create","update" -> {
                    int    id     = p.get("id").asInt();
                    String from   = p.get("from").asText();
                    String to     = p.get("to").asText();
                    String dep    = p.get("dateDeparture").asText();
                    String arr    = p.get("dateArrival").asText();
                    int    seats  = p.get("seats").asInt();
                    int    compId = p.get("companyId").asInt();
                    yield M.valueToTree(
                            action.equals("create")
                                    ? fltSrv.createFlight(id, from, to, dep, arr, seats, compId)
                                    : fltSrv.updateFlight(id, from, to, dep, arr, seats, compId)
                    );
                }
                case "delete"    -> M.valueToTree(fltSrv.deleteFlight(p.get("id").asInt()));
                default          -> throw new IllegalArgumentException("Unknown flight action: " + action);
            };
            case "company" -> switch (action) {
                case "findAll"   -> M.valueToTree(cmpSrv.findAll());
                case "findById"  -> M.valueToTree(cmpSrv.findById(p.get("id").asInt()));
                case "create","update" -> {
                    int    id  = p.get("id").asInt();
                    String nm  = p.get("name").asText();
                    String loc = p.get("location").asText();
                    yield M.valueToTree(
                            action.equals("create")
                                    ? cmpSrv.createFlightCompany(id, nm, loc)
                                    : cmpSrv.updateFlightCompany(id, nm, loc)
                    );
                }
                case "delete"    -> M.valueToTree(cmpSrv.deleteFlightCompany(p.get("id").asInt()));
                default          -> throw new IllegalArgumentException("Unknown company action: " + action);
            };
            case "user" -> switch (action) {
                case "findAll"   -> M.valueToTree(usrSrv.findAll());
                case "findById"  -> M.valueToTree(usrSrv.findById(p.get("id").asInt()));
                case "create","update" -> {
                    int    id = p.get("id").asInt();
                    String un = p.get("username").asText();
                    String pw = p.get("password").asText();
                    yield M.valueToTree(
                            action.equals("create")
                                    ? usrSrv.createUser(id, un, pw)
                                    : usrSrv.updateUser(id, un, pw)
                    );
                }
                case "delete"    -> M.valueToTree(usrSrv.deleteUser(p.get("id").asInt()));
                default          -> throw new IllegalArgumentException("Unknown user action: " + action);
            };
            case "ticket" -> switch (action) {
                case "findAll"   -> M.valueToTree(tckSrv.findAllTickets());
                case "findById"  -> M.valueToTree(tckSrv.findById(p.get("id").asInt()));
                case "create"    -> M.valueToTree(tckSrv.createTicket(
                        p.get("id").asInt(),
                        p.get("flightId").asInt(),
                        p.get("touristId").asInt(),
                        p.get("employeeId").asInt(),
                        p.get("seatType").asText(),
                        p.get("seatNumber").asText(),
                        p.get("customerName").asText()));
                case "update"    -> M.valueToTree(tckSrv.updateTicket(
                        p.get("id").asInt(),
                        p.get("flightId").asInt(),
                        p.get("seatType").asText(),
                        p.get("seatNumber").asText(),
                        p.get("customerName").asText()));
                case "delete"    -> M.valueToTree(tckSrv.deleteTicket(p.get("id").asInt()));
                default          -> throw new IllegalArgumentException("Unknown ticket action: " + action);
            };
            case "tourist" -> switch (action) {
                case "findAll"   -> M.valueToTree(trsSrv.findAll());
                case "findById"  -> M.valueToTree(trsSrv.findById(p.get("id").asInt()));
                case "create","update" -> {
                    int    id = p.get("id").asInt();
                    String nm = p.get("name").asText();
                    yield M.valueToTree(
                            action.equals("create")
                                    ? trsSrv.createTourist(id, nm)
                                    : trsSrv.updateTourist(id, nm)
                    );
                }
                case "delete"    -> M.valueToTree(trsSrv.deleteTourist(p.get("id").asInt()));
                default          -> throw new IllegalArgumentException("Unknown tourist action: " + action);
            };
            case "agency" -> switch (action) {
                case "findAll"   -> M.valueToTree(agySrv.findAll());
                case "findById"  -> M.valueToTree(agySrv.findById(p.get("id").asInt()));
                case "create","update" -> {
                    int    id       = p.get("id").asInt();
                    String nm       = p.get("name").asText();
                    int    empCnt   = p.get("employees").asInt();
                    String fb       = p.get("flightsBooked").asText();
                    yield M.valueToTree(
                            action.equals("create")
                                    ? agySrv.createTravelAgency(id, nm, empCnt, fb)
                                    : agySrv.updateTravelAgency(id, nm, empCnt, fb)
                    );
                }
                case "delete"    -> M.valueToTree(agySrv.deleteTravelAgency(p.get("id").asInt()));
                default          -> throw new IllegalArgumentException("Unknown agency action: " + action);
            };
            default -> throw new IllegalArgumentException("Unknown service: " + svc);
        };
    }
}
