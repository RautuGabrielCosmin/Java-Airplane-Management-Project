package server;

import repository.MemoryEmployee;
import repository.MemoryFlight;
import repository.MemoryFlightCompany;
import repository.MemoryTicket;
import repository.MemoryTourist;
import repository.MemoryTravelAgency;
import repository.MemoryUser;
import service.EmployeeService;
import service.FlightService;
import service.FlightCompanyService;
import service.TicketService;
import service.TouristService;
import service.TravelAgencyService;
import service.UserService;
import service.MemoryEmployeeService;
import service.MemoryFlightService;
import service.MemoryFlightCompanyService;
import service.MemoryTicketService;
import service.MemoryTouristService;
import service.MemoryTravelAgencyService;
import service.MemoryUserService;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds your singleton service instances and the set of all active client handlers
 * so that we can broadcast messages to everyone.
 */
public final class AppContext {
    private AppContext() { /* no instantiation */ }

    //--- service singletons
    public static final EmployeeService        EMP_SRV = new MemoryEmployeeService(new MemoryEmployee());
    public static final FlightService          FLT_SRV = new MemoryFlightService(new MemoryFlight());
    public static final FlightCompanyService   CMP_SRV = new MemoryFlightCompanyService(new MemoryFlightCompany());
    public static final UserService            USR_SRV = new MemoryUserService(new MemoryUser());
    public static final TicketService          TCK_SRV = new MemoryTicketService(new MemoryTicket());
    public static final TouristService         TRS_SRV = new MemoryTouristService(new MemoryTourist());
    public static final TravelAgencyService    AGY_SRV = new MemoryTravelAgencyService(new MemoryTravelAgency());

    //--- the set of all currently connected handlers, for broadcasting
    private static final Set<ClientHandler> clients =
            Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static void register(ClientHandler handler) {
        clients.add(handler);
    }

    public static void unregister(ClientHandler handler) {
        clients.remove(handler);
    }

    public static Set<ClientHandler> getAllClients() {
        return clients;
    }
}
