package repository;
import domain.TravelAgency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("CallToPrintStackTrace")
public class MemoryTravelAgency implements TravelAgencyRepository {
    private final static Logger logger = LoggerFactory.getLogger(MemoryTravelAgency.class);

    @Override
    public TravelAgency add(Integer id) {
        TravelAgency travelAgency = new TravelAgency();
        travelAgency.setIdTravelAgency(id);
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "INSERT INTO travel_agency (id) Values (?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            logger.info("Travel Agency Added with id " + id);
        }catch(SQLException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(ps != null){
                try{
                    ps.close();
                    logger.info("Prepared Statement Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                e.printStackTrace();}
                }
            if(conn != null){
                try{
                    conn.close();
                    logger.info("Connection Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return travelAgency;
    }

    @Override
    public TravelAgency findById(Integer id) {
        TravelAgency travelAgency = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new ConnectDB().getConnection();
            String sql = "SELECT * FROM travel_agency WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                try {
                    travelAgency = new TravelAgency();
                    travelAgency.setIdTravelAgency(rs.getInt("id"));
                    travelAgency.setName(rs.getString("name"));
                    travelAgency.setNumberOfEmployees(rs.getInt("number_of_employees"));
                    travelAgency.setFlightsBooked(rs.getString("flights_booked"));
                    logger.info("Travel Agency Found with id " + id);
                }catch (SQLException e) {
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }catch (SQLException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(rs != null){
                try{
                    rs.close();
                    logger.info("Result Set Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Prepared Statement Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Connection Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return travelAgency;
    }

    @Override
    public List<TravelAgency> findAll() {
        List<TravelAgency> travelAgencies = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "SELECT * FROM travel_agency";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                TravelAgency travelAgency = new TravelAgency();
                travelAgency.setIdTravelAgency(rs.getInt("id"));
                travelAgency.setName(rs.getString("name"));
                travelAgency.setNumberOfEmployees(rs.getInt("number_of_employees"));
                travelAgency.setFlightsBooked(rs.getString("flights_booked"));
                travelAgencies.add(travelAgency);
                logger.info("Travel Agency Found with id " + rs.getInt("id"));
            }
        } catch (SQLException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(rs != null) {
                try{
                    rs.close();
                    logger.info("Result Set Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Prepared Statement Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Connection Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return travelAgencies;
    }

    @Override
    public TravelAgency save(TravelAgency travelAgency) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "INSERT OR REPLACE INTO travel_agency (id, name, number_of_employees, " +
                    "flights_booked) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                ps.setInt(1, travelAgency.getIdTravelAgency());
                ps.setString(2, travelAgency.getName());
                ps.setInt(3, travelAgency.getNumberOfEmployees());
                ps.setString(4, travelAgency.getFlightsBooked());
                ps.executeUpdate();
                logger.info("Travel Agency Saved with id " + travelAgency.getIdTravelAgency());
            }
        }catch (SQLException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(rs != null){
                try{
                    rs.close();
                    logger.info("Result Set Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Prepared Statement Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Connection Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return travelAgency;
    }

    @Override
    public TravelAgency update(TravelAgency travelAgency) {
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "UPDATE travel_agency SET name = ?,number_of_employees = ?,flights_booked = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, travelAgency.getIdTravelAgency());
            ps.setString(2, travelAgency.getName());
            ps.setInt(3, travelAgency.getNumberOfEmployees());
            ps.setString(4, travelAgency.getFlightsBooked());
            logger.info("Travel Agency Updated");
        }catch (SQLException e) {
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Prepared Statement Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Connection Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return travelAgency;
    }

    @Override
    public TravelAgency delete(Integer id) {
        TravelAgency deleteTravelAgency = findById(id);
        if(deleteTravelAgency != null){
            logger.info("Travel Agency not found with id " + id);
            return null;
        }
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "DELETE FROM travel_agency WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            logger.info("Travel Agency Deleted with id " + id);
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Prepared Statement Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Connection Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return deleteTravelAgency;
    }
}