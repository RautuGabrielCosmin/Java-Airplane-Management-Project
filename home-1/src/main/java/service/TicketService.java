package service;

import domain.Ticket;

import java.util.List;

public interface TicketService {
    Ticket createTicket(int id, int flightId, int touristId, int employeeId, String seatType, String seatNumber,
                        String customerName);
    Ticket findById(int id);
    List<Ticket> findAllTickets();
    Ticket updateTicket(int id, String newFlightId, String newSeatType, String newSeatNumber, String newCustomerName);
    Ticket deleteTicket(int id);
}
