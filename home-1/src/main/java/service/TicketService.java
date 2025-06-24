package service;

import domain.Ticket;

import java.io.IOException;
import java.util.List;

public interface TicketService {
    Ticket createTicket(int id, int flightId, int touristId, int employeeId, String seatType, String seatNumber,
                        String customerName) throws IOException;
    Ticket findById(int id) throws IOException;

    List<Ticket> findAllTickets() throws IOException;
    Ticket updateTicket(int id, int newFlightId, String newSeatType, String newSeatNumber, String newCustomerName) throws IOException;
    Ticket deleteTicket(int id) throws IOException;
}
