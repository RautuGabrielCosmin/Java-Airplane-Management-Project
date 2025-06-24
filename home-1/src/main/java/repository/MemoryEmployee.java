package repository;

import domain.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class MemoryEmployee implements EmployeeRepository {
    private final static Logger logger = LoggerFactory.getLogger(MemoryEmployee.class);

    public Employee add(Integer id) {
        Employee employee = new Employee();
        employee.setIdEmployee(id);
        employee.setName("DefaultName");
        Connection conn = null;
        PreparedStatement ps = null;
        logger.info("Adding employee with {} using the name {} ", id, employee.getName());
        try{
            conn = new ConnectDB().getConnection();
            String sql = "INSERT INTO employee (id, name) VALUES (?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, employee.getIdEmployee());
            ps.setString(2, employee.getName());
            ps.executeUpdate();
            logger.info("Employee added successfully {}", id);
        } catch (SQLException e) {
            logger.debug("Error adding employee {}", id, e);
            e.printStackTrace();
        }finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Closing prepared statement");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement {}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Closing connection to database");
                }catch(SQLException e){
                    logger.debug("Error closing connection to database", e);
                    e.printStackTrace();
                }
            }
        }
        logger.info("Employee added successfully {} with the name {}", id, employee.getName());
        return employee;
    }

    public Employee findById(Integer id) {
        Employee employee = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        logger.info("Find employee by id {}", id);
        try{
            conn = new ConnectDB().getConnection();
            String sql = "SELECT * FROM employee WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                employee = new Employee();
                employee.setIdEmployee(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                logger.info("Found employee with id {}", id);
            }
        }catch (SQLException e) {
            logger.debug("Error finding employee {}", id, e);
            e.printStackTrace();
        }finally{
            if(rs != null) {
                try{
                    rs.close();
                    logger.info("Closing prepared statement");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement", e);
                    e.printStackTrace();
                }
            }
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Closing prepared statement");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement", e);
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Closing connection to database");
                }catch(SQLException e){
                    logger.debug("Error closing connection to database", e);
                    e.printStackTrace();
                }
            }
        }
        return employee;
    }

    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        logger.info("Find all employees");
        try{
            conn = new ConnectDB().getConnection();
            String sql = "SELECT * FROM employee";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while(rs.next()){
                Employee employee = new Employee();
                employee.setIdEmployee(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employees.add(employee);
                logger.info("Found employee with id {}", rs.getInt( "id"));
            }

        }catch (SQLException e) {
            logger.debug("Error finding all employees", e);
            e.printStackTrace();
        }finally{
            if(rs != null) {
                try{
                    rs.close();
                    logger.info("Closing prepared statement");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement", e);
                    e.printStackTrace();
                }
            }
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Closing prepared statement");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement", e);
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Closing connection to database");
                }catch(SQLException e){
                    logger.debug("Error closing connection to database", e);
                    e.printStackTrace();
                }
            }
        }
        logger.info("Find all employees successfully");
        return employees;
    }

    public Employee save(Employee employee) {
        Connection conn = null;
        PreparedStatement ps = null;
        logger.info("Saving employee with id {}", employee.getIdEmployee());
        try{
            conn = new ConnectDB().getConnection();
            String sql = "INSERT OR REPLACE INTO employee (id, name) VALUES (?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,employee.getIdEmployee());
            ps.setString(2,employee.getName());
            ps.executeUpdate();
            logger.info("Employee added successfully {}", employee.getIdEmployee());
        }catch(SQLException e){
            logger.debug("Error saving the employee to the database",e.getMessage());
            e.printStackTrace();
        }finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Closing prepared statement");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement", e);
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Closing connection to database");
                }catch(SQLException e){
                    logger.debug("Error closing connection to database", e);
                    e.printStackTrace();
                }
            }
        }
        return employee;
    }

    public Employee update(Employee employee) {
        Connection conn = null;
        PreparedStatement ps = null;
        logger.info("Updating employee with id {}", employee.getIdEmployee());
        try{
            conn = new ConnectDB().getConnection();
            String sql = "UPDATE employee SET name = ? WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, employee.getName());
            ps.setInt(2, employee.getIdEmployee());
            ps.executeUpdate();
            logger.info("Employee updated successfully {}", employee.getIdEmployee());
        }catch (SQLException e){
            logger.debug("Error updating the employee to the database ", e.getMessage());
            e.printStackTrace();
        }finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Closing prepared statement");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement", e);
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Closing connection to database");
                }catch(SQLException e){
                    logger.debug("Error closing connection to database", e);
                    e.printStackTrace();
                }
            }
        }
        return employee;
    }

    public Employee delete(Integer id) {
        Employee deleteEmployee = findById(id);
        if(deleteEmployee == null){
            logger.info("No employee with id {}", id);
            return null;
        }
        logger.info("Deleting employee with id {}", id);
        Connection conn = null;
        PreparedStatement ps = null;
        try{
            conn = new ConnectDB().getConnection();
            String sql = "DELETE FROM employee WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            logger.info("Employee deleted successfully {}", id);
        }catch (SQLException e){
            logger.debug("Error deleting the employee to the database",e.getMessage());
            e.printStackTrace();
        }finally{
            if(ps != null) {
                try{
                    ps.close();
                    logger.info("Closing prepared statement");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement", e);
                    e.printStackTrace();
                }
            }
            if(conn != null) {
                try{
                    conn.close();
                    logger.info("Closing connection to database");
                }catch(SQLException e){
                    logger.info("Error closing prepared statement", e);
                    e.printStackTrace();
                }
            }
        }
        return deleteEmployee;
    }
}
