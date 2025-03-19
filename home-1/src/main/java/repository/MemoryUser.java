package repository;

import desktop_app.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("CallToPrintStackTrace")
public class MemoryUser implements UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(MemoryUser.class);

    public User add(Integer id){
        User user = new User();
        Connection con = null;
        PreparedStatement ps = null;
        try{
            con = new ConnectDB().getConnection();
            String sql = "insert into user (id, username, password) values (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
            logger.info("User added successfully with id " + id);
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Prepared Statement closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(con != null) {
                try{
                    con.close();
                    logger.info("Connection Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    public User findById(Integer id){
        User user = new User();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            con = new ConnectDB().getConnection();
            String sql = "SELECT * FROM user WHERE id = ?";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                logger.info("User found with id " + id);
            }
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(rs != null) {
                try{
                    rs.close();
                    logger.info("ResultSet Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("PreparedStatement Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(con != null) {
                try{
                    con.close();
                    logger.info("Connection Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    public List<User> findAll(){
        List<User> listOfUsers = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            con = new ConnectDB().getConnection();
            String sql = "SELECT * FROM user";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                listOfUsers.add(user);
                logger.info("User found with id " + user.getId());
            }
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(rs != null) {
                try{
                    rs.close();
                    logger.info("ResultSet Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("PreparedStatement Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(con != null) {
                try{
                    con.close();
                    logger.info("Connection Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return listOfUsers;
    }

    public User save(User user){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            con = new ConnectDB().getConnection();
            String sql = "INSERT OR REPLACE INTO user (id, username, password) VALUES (?,?,?)";
            ps = con.prepareStatement(sql);
            ps.setInt(1, user.getId());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
            logger.info("User saved successfully");
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("PreparedStatement Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(con != null) {
                try{
                    con.close();
                    logger.info("Connection Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return user;
    }

    public User update(User user){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            con = new ConnectDB().getConnection();
            String sql = "update user set username = ?, password = ? where id = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setInt(3, user.getId());
            ps.executeUpdate();
            logger.info("User updated successfully");
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("PreparedStatement Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(con != null) {
                try{
                    con.close();
                    logger.info("Connection Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return user;

    }

    public User delete(Integer id){
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = findById(id);
        if(user == null){
            logger.info("User with id " + id + " not found");
            return null;
        }
        try{
            con = new ConnectDB().getConnection();
            String sql = "delete from user where id = ?";
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            logger.info("User deleted successfully");
        }catch (SQLException e){
            logger.debug(e.getMessage());
            e.printStackTrace();
        }finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("PreparedStatement Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
            if(con != null) {
                try{
                    con.close();
                    logger.info("Connection Closed");
                }catch(SQLException e){
                    logger.debug(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return user;
    }
}
