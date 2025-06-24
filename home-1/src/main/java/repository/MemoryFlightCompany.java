package repository;

import domain.FlightCompany;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class MemoryFlightCompany extends FlightCompany implements FlightCompanyRepository {
    private final static Logger logger = LoggerFactory.getLogger(MemoryFlightCompany.class);

    public FlightCompany add(Integer id){
        FlightCompany fc = new FlightCompany();
        fc.setIdFlight(id);
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "INSERT INTO flight_company(id, name, location) VALUES(?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,fc.getIdFlight());
            ps.setString(2,fc.getName());
            ps.setString(3,fc.getLocation());
            ps.executeUpdate();
            logger.info("Added flight company");
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
                    logger.info("Closed connection");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return fc;
    }

    public FlightCompany findById(Integer id){
        FlightCompany fc = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "SELECT * FROM flight_company WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            if(rs.next()){
                fc = new FlightCompany();
                fc.setIdFlight(rs.getInt("id"));
                fc.setName(rs.getString("name"));
                fc.setLocation(rs.getString("location"));
                logger.info("Found flight company with {} ", id);
            }
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(rs != null) {
                try{
                    rs.close();
                    logger.info("ResultSet closed");
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
                    logger.info("Closed connection");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return fc;
    }

    public List<FlightCompany> findAll(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<FlightCompany> list = new ArrayList<>();
        try{
            conn = new ConnectDB().getConnection();
            String sql = "SELECT * FROM flight_company";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                FlightCompany fc = new FlightCompany();
                fc.setIdFlight(rs.getInt("id"));
                fc.setName(rs.getString("name"));
                fc.setLocation(rs.getString("location"));
                list.add(fc);
                logger.info("Found flight company with {} ", rs.getInt("id"));
            }
        }catch (SQLException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }finally{
            if(rs != null) {
                try{
                    rs.close();
                    logger.info("ResultSet closed");
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
                    logger.info("Closed connection");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public FlightCompany save(FlightCompany fc) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = new ConnectDB().getConnection();
            String sql = "INSERT OR REPLACE INTO flight_company (id, name, location) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, fc.getIdFlight());
            ps.setString(2, fc.getName());
            ps.setString(3, fc.getLocation());
            ps.executeUpdate();
            logger.info("Saved FlightCompany: {}", fc);
        } catch (SQLException e) {
            logger.debug("Error saving FlightCompany", e);
            e.printStackTrace();
        } finally{
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
                    logger.info("Closed connection");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return fc;
    }

    public FlightCompany update(FlightCompany fc){
        Connection conn = null;
        PreparedStatement ps = null;
        fc = new FlightCompany();
        try{
            conn = new ConnectDB().getConnection();
            String sql = "UPDATE flight_company SET name = ?, location = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1,fc.getName());
            ps.setString(2,fc.getLocation());
            ps.setInt(3,fc.getIdFlight());
            ps.executeUpdate();
            logger.info("Updated flight company");
        }catch(SQLException e){
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
                    logger.info("Closed connection");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return fc;
    }

    public FlightCompany delete(Integer id){
        Connection conn = null;
        PreparedStatement ps = null;
        FlightCompany deleteFc = findById(id);
        if(deleteFc == null){
            logger.info("No flight company found with id {}", id);
            return null;
        }
        try{
            conn = new ConnectDB().getConnection();
            String sql = "DELETE FROM flight_company WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
            logger.info("Deleted flight company");
        }catch(SQLException e){
            logger.trace(e.getMessage());
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
                    logger.info("Closed connection");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return deleteFc;
    }
}
