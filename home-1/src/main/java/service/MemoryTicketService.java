package service;

import domain.Ticket;
import repository.TicketRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemoryTicketService implements TicketService {
    private static final Logger logger = LoggerFactory.getLogger(MemoryTicketService.class);

    public final TicketRepository ticketRepository;
    public MemoryTicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Ticket createTicket(int id, int flightId, int touristId, int employeeId, String seatType, String seatNumber,
                               String customerName){
        logger.info("Creating ticket with id {}, tourist={}, employee with the id {}," +
                "seat type={}, seat with the number={}," +
                "and the customer with the name={}",id,flightId,touristId,employeeId,seatType,seatNumber,customerName);
        if(seatType == null || seatType.equals("")){
            logger.error("Seat type is empty");
            throw new RuntimeException("Seat type is empty");
        }
        if(seatNumber == null || seatNumber.equals("")){
            logger.error("Seat number is empty");
            throw new RuntimeException("Seat number is empty");
        }
        if(customerName == null || customerName.equals("")){
            logger.error("Customer name is empty");
            throw new RuntimeException("Customer name is empty");
        }
        Ticket ticket = ticketRepository.findById(id);
        if(ticket != null && ticket.getId() !=0){
            logger.error("Ticket with id {} already exists", ticket.getId());
            throw new RuntimeException("Ticket with id {} already exists");
        }
        Ticket ticket = new Ticket(id,flightId,touristId,employeeId,seatType,seatNumber,customerName);
        Ticket savedTicket = ticketRepository.save(ticket);
        logger.info("Ticket created successfully");
        return savedTicket;
    }

}
