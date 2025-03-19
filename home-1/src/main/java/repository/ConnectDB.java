package repository;

//import org.sqlite.util.Logger;
//import org.sqlite.util.LoggerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

@SuppressWarnings("CallToPrintStackTrace")
public class ConnectDB {
    Logger logger = LoggerFactory.getLogger(ConnectDB.class);
    private Connection con = null;

    public ConnectDB() {
        String url = "jdbc:sqlite:C:\\Users\\Rautu\\Desktop\\MPP\\Homework-1\\home-1\\data\\app.db";
        try{
            con = DriverManager.getConnection(url);
            logger.info("Connected to the database");
        } catch (SQLException e) {
            logger.error("Error connecting to the database");
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return con;
    }

    public void closeConnection(){
        try{
            if(con != null){
                con.close();
                logger.info("Connection closed");
            }
        } catch (SQLException e) {
            logger.error("Error closeing the connection to the database {}", e.getMessage());
            e.printStackTrace();
        }
    }
}
