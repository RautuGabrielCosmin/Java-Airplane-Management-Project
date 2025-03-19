package repository;

import domain.Tourist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("CallToPrintStackTrace")
public class MemoryTourist implements TouristRepository {
    private static final Logger logger = LoggerFactory.getLogger(MemoryTourist.class);

    public Tourist add(Integer id){
        Tourist tourist = new Tourist();
        Connection con = null;
        PreparedStatement ps = null;
        try{
            con = new ConnectDB().getConnection();
            String sql = "INSERT INTO tourist (id, name) VALUES (?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, tourist.getName());
            ps.executeUpdate();
            logger.info("Inserted tourist with the id {} and name {}", id, tourist.getName());
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
            if(con != null) {
                try{
                    con.close();
                    logger.info("Closed connection");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return tourist;
    }

    public Tourist findById(Integer id){
        Tourist tourist = new Tourist();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            con = new ConnectDB().getConnection();
            String sql = "SELECT * FROM tourist WHERE id = ?";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                tourist = new Tourist();
                tourist.setIdTourist(rs.getInt("idTourist"));
                tourist.setName(rs.getString("name"));
                logger.info("Found tourist with the id {} and name {}", id, tourist.getName());
            }
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(rs != null) {
                try{
                    rs.close();
                    logger.info("Closed statement closed");
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
            if(con != null) {
                try{
                    con.close();
                    logger.info("Closed connection");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return tourist;
    }

    public List<Tourist> findAll(){
        List<Tourist> listOfTourists = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            con = new ConnectDB().getConnection();
            String sql = "SELECT * FROM tourist";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Tourist tourist = new Tourist();
                tourist.setIdTourist(rs.getInt("idTourist"));
                tourist.setName(rs.getString("name"));
                listOfTourists.add(tourist);
                logger.info("All the tourists found with id {} and name {}", rs.getInt("idTourist"), rs.getString("name"));
            }
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(rs != null) {
                try{
                    rs.close();
                    logger.info("Closed statement closed");
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
            if(con != null) {
                try{
                    con.close();
                    logger.info("Closed connection");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return listOfTourists;
    }

    public Tourist save(Tourist tourist){
        Connection con = null;
        PreparedStatement ps = null;
        try{
            con = new ConnectDB().getConnection();
            String sql = "INSERT OR REPLACE INTO tourist (id, name) VALUES (?,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, tourist.getName());
            ps.setInt(2, tourist.getIdTourist());
            ps.executeUpdate();
            logger.info("Tourist {} saved", tourist.getName());
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
            if(con != null) {
                try{
                    con.close();
                    logger.info("Closed connection");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return tourist;
    }

    public Tourist update(Tourist tourist){
        Connection con = null;
        PreparedStatement ps = null;
        try{
            con = new ConnectDB().getConnection();
            String sql = "UPDATE tourist SET name = ? WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, tourist.getName());
            ps.setInt(2, tourist.getIdTourist());
            ps.executeUpdate();
            logger.info("Tourist {} updated", tourist.getName());
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
            if(con != null) {
                try{
                    con.close();
                    logger.info("Closed connection");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return tourist;
    }

    public Tourist delete(Integer id){
        Tourist tourist1 = findById(id);
        if(tourist1 == null){
            logger.info("Tourist with id {} not found", id);
            return null;
        }
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            con = new ConnectDB().getConnection();
            String sql = "DELETE FROM tourist WHERE id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            logger.info("Tourist {} deleted", id);
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
            if(con != null) {
                try{
                    con.close();
                    logger.info("Closed connection");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return tourist1;
    }
}
