package repository;

import domain.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class MemoryTicket implements TicketRepository {
    private static final Logger logger = LoggerFactory.getLogger(MemoryTicket.class);

    public Ticket add(Integer id){
        Ticket ticket = new Ticket();
        ticket.setId(id);
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "INSERT INTO ticket (id, flight_id, tourist_id, employee_id, seat_type" +
                    ", seat_number, customer_name) VALUES (?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, ticket.getFlightId());
            ps.setInt(3, ticket.getTouristId());
            ps.setInt(4, ticket.getEmployeeId());
            ps.setString(5, ticket.getSeatType());
            ps.setString(6, ticket.getSeatNumber());
            ps.setString(7, ticket.getCustomerName());
            ps.executeUpdate();
            logger.info("Ticket added successfully");
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }
        finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Prepared statement closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Connection closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return ticket;
    }

    public Ticket findById(Integer id){
        Ticket ticket = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "SELECT * FROM ticket WHERE id = ?";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            ps.setInt(1, id);
            if(rs.next()){
                ticket = new Ticket();
                ticket.setId(id);
                ticket.setFlightId(rs.getInt("flightId"));
                ticket.setTouristId(rs.getInt("touristId"));
                ticket.setEmployeeId(rs.getInt("employeeId"));
                ticket.setSeatType(rs.getString("seatType"));
                ticket.setSeatNumber(rs.getString("seatNumber"));
                ticket.setCustomerName(rs.getString("customerName"));
                logger.info("Ticket found successfully by {} ", id);
            }
        }catch (SQLException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(rs != null) {
                try{
                    rs.close();
                    logger.info("Result Set closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Prepared statement closed");
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Connection closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return ticket;
    }

    public List<Ticket> findAll(){
        List<Ticket> listOfTickets = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "SELECT * FROM ticket";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Ticket ticket = new Ticket();
                ticket.setId(rs.getInt("id"));
                ticket.setFlightId(rs.getInt("flightId"));
                ticket.setTouristId(rs.getInt("touristId"));
                ticket.setEmployeeId(rs.getInt("employeeId"));
                ticket.setSeatType(rs.getString("seatType"));
                ticket.setSeatNumber(rs.getString("seatNumber"));
                ticket.setCustomerName(rs.getString("customerName"));
                listOfTickets.add(ticket);
                logger.info("Ticket found successfully by {} ", rs.getInt("id"));
            }
        }catch(SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(rs != null) {
                try{
                    rs.close();
                    logger.info("Result Set closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Prepared statement closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Connection closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return listOfTickets;
    }

    public Ticket save(Ticket ticket){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "INSERT OR REPLACE INTO ticket (id, flight_id, tourist_id, employee_id, seat_type, " +
                    "seat_number, customer_name) VALUES (?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ticket.getId());
            ps.setInt(2, ticket.getFlightId());
            ps.setInt(3, ticket.getTouristId());
            ps.setInt(4, ticket.getEmployeeId());
            ps.setString(5, ticket.getSeatType());
            ps.setString(6, ticket.getSeatNumber());
            ps.setString(7, ticket.getCustomerName());
            ps.executeUpdate();
            logger.info("Ticket added successfully");
        }catch(SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }
        finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Prepared statement closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Connection closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return ticket;
    }

    public Ticket update(Ticket ticket){
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "UPDATE ticket SET flight_id = ?, tourist_id = ?, employee_id = ?, seat_type = ?, " +
                    "seat_number = ?, customer_name = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, ticket.getFlightId());
            ps.setInt(2, ticket.getTouristId());
            ps.setInt(3, ticket.getEmployeeId());
            ps.setString(4, ticket.getSeatType());
            ps.setString(5, ticket.getSeatNumber());
            ps.setString(6, ticket.getCustomerName());
            ps.setInt(7, ticket.getId());
            ps.executeUpdate();
            logger.info("Ticket updated");
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Prepared statement closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Connection closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return ticket;
    }

    public Ticket delete(Integer id){
        Ticket ticket1 = findById(id);
        if(ticket1 == null){
            logger.info("Ticket not found");
            return null;
        }
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "DELETE FROM ticket WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            logger.info("Ticket deleted successfully with the id {}", id);
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Prepared statement closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Connection closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return ticket1;
    }
}
