package repository;

import domain.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("CallToPrintStackTrace")
public class MemoryFlight implements FlightRepository {
    private static final Logger logger = LoggerFactory.getLogger(MemoryFlight.class);

    public Flight add(Integer id){
        Flight flight = new Flight();
        flight.setId(id);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = new ConnectDB().getConnection();
            String sql = "INSERT INTO flight VALUES(?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,flight.getId());
            preparedStatement.setString(2,flight.getLocationDeparture());
            preparedStatement.setString(3,flight.getLocationArrival());
            preparedStatement.setString(4,flight.getDateDeparture());
            preparedStatement.setString(5,flight.getDateArrival());
            preparedStatement.setInt(6,flight.getNumberOfSeats());
            preparedStatement.setInt(7,flight.getFlightCompanyId());
            preparedStatement.executeUpdate();
            logger.info("Flight added successfully");
        }catch (SQLException e){
            logger.debug("Error adding to the database {}", e.getMessage());
            e.printStackTrace();
        }finally{
            if(preparedStatement != null) {
                try{
                    preparedStatement.close();
                    logger.info("Prepared statement closed");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement {}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try{
                    connection.close();
                    logger.info("Connection closed");
                }catch(SQLException e){
                    logger.debug("Error closing connection {}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return flight;
    }

    public Flight findById(Integer id){
        Flight flight = new Flight();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = new ConnectDB().getConnection();
            String sql = "SELECT * FROM flight WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                flight = new Flight();
                flight.setId(resultSet.getInt("id"));
                flight.setLocationDeparture(resultSet.getString("location_departure"));
                flight.setLocationArrival(resultSet.getString("location_arrival"));
                flight.setDateDeparture(resultSet.getString("date_departure"));
                flight.setDateArrival(resultSet.getString("date_arrival"));
                flight.setNumberOfSeats(resultSet.getInt("number_of_seats"));
                flight.setFlightCompanyId(resultSet.getInt("flight_company_id"));
                logger.info("Flight found successfully by id {}", id);
            }
        }catch (SQLException e){
            logger.debug("Error finding flight {}", e.getMessage());
            e.printStackTrace();
        }finally{
            if(resultSet != null) {
                try{
                    resultSet.close();
                    logger.info("ResultSet closed");
                }catch(SQLException e){
                    logger.debug("Error closing resultSet {}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null) {
                try{
                    preparedStatement.close();
                    logger.info("Prepared statement closed");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement {}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try{
                    connection.close();
                    logger.info("Connection closed");
                }catch(SQLException e){
                    logger.debug("Error closing connection {}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return flight;
    }

    public List<Flight> findAll(){
        List<Flight> flights = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = new ConnectDB().getConnection();
            String sql = "SELECT * FROM flight";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Flight flight = new Flight();
                flight.setId(resultSet.getInt("id"));
                flight.setLocationDeparture(resultSet.getString("location_departure"));
                flight.setLocationArrival(resultSet.getString("location_arrival"));
                flight.setDateDeparture(resultSet.getString("date_departure"));
                flight.setDateArrival(resultSet.getString("date_arrival"));
                flight.setNumberOfSeats(resultSet.getInt("number_of_seats"));
                flight.setFlightCompanyId(resultSet.getInt("flight_company_id"));
                flights.add(flight);
                logger.info("Flight found successfully by id {}", flight.getId());
            }
        }catch (SQLException e){
            logger.debug("Error finding flight {}", e.getMessage());
            e.printStackTrace();
        }finally{
            if(resultSet != null) {
                try{
                    resultSet.close();
                    logger.info("ResultSet closed");
                }catch(SQLException e){
                    logger.debug("Error closing resultSet {}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if(preparedStatement != null) {
                try{
                    preparedStatement.close();
                    logger.info("Prepared statement closed");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement {}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try{
                    connection.close();
                    logger.info("Connection closed");
                }catch(SQLException e){
                    logger.debug("Error closing connection {}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return flights;
    }

    public Flight update(Flight flight){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = new ConnectDB().getConnection();
            String sql = "UPDATE flight SET location_departure=?, location_arrival=?, date_departure=?, " +
                    "date_arrival=?, number_of_seats=?, flight_company_id=? WHERE id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,flight.getLocationDeparture());
            preparedStatement.setString(2,flight.getLocationArrival());
            preparedStatement.setString(3,flight.getDateDeparture());
            preparedStatement.setString(4,flight.getDateArrival());
            preparedStatement.setInt(5,flight.getNumberOfSeats());
            preparedStatement.setInt(6,flight.getFlightCompanyId());
            preparedStatement.setInt(7,flight.getId());
            preparedStatement.executeUpdate();
            logger.info("Flight updated successfully by id {}", flight.getId());
        }catch (SQLException e){
            logger.debug("Error updating flight {}", e.getMessage());
            e.printStackTrace();
        }finally{
            if(preparedStatement != null) {
                try{
                    preparedStatement.close();
                    logger.info("Prepared statement closed");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement {}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try{
                    connection.close();
                    logger.info("Connection closed");
                }catch(SQLException e){
                    logger.debug("Error closing connection", e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return flight;
    }

    public Flight save(Flight flight){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = new ConnectDB().getConnection();
            String sql = "INSERT OR REPLACE INTO flight (id, location_departure, location_arrival, date_departure, " +
                    "date_arrival, number_of_seats, flight_company_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,flight.getFlightCompanyId());
            preparedStatement.setString(2,flight.getLocationDeparture());
            preparedStatement.setString(3,flight.getLocationArrival());
            preparedStatement.setString(4,flight.getDateDeparture());
            preparedStatement.setString(5,flight.getDateArrival());
            preparedStatement.setInt(6,flight.getNumberOfSeats());
            preparedStatement.setInt(7,flight.getId());
            preparedStatement.executeUpdate();
            logger.info("Flight saved successfully");
        }catch (SQLException e){
            logger.debug("Error adding flight {}", e.getMessage());
            e.printStackTrace();
        }finally{
            if(preparedStatement != null) {
                try{
                    preparedStatement.close();
                    logger.info("Prepared statement closed");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement", e.getMessage());
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try{
                    connection.close();
                    logger.debug("Connection closed");
                }catch(SQLException e){
                    logger.debug("Error closing connection", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return flight;
    }

    public Flight delete(Integer id){
        Flight deleteFlight = findById(id);
        if(deleteFlight == null){
            logger.info("Flight not found");
            return null;
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = new ConnectDB().getConnection();
            String sql = "DELETE FROM flight WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            logger.info("Flight deleted successfully");

        }catch (SQLException e){
            e.printStackTrace();
            logger.debug("Error deleting flight {}", e.getMessage());
        }finally{
            if(preparedStatement != null) {
                try{
                    preparedStatement.close();
                    logger.info("Prepared statement closed");
                }catch(SQLException e){
                    logger.debug("Error closing prepared statement {}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if(connection != null) {
                try{
                    connection.close();
                    logger.info("Connection closed");
                }catch(SQLException e){
                    logger.debug("Error closing connection {}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return deleteFlight;
    }
}
