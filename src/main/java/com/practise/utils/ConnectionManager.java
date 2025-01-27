package com.practise.utils;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class ConnectionManager {
    private static final Logger LOGGER = Logger.getLogger(ConnectionManager.class.getName());

    private static Connection connection;
    private static String url;
    private static String username;
    private static String password;

    private ConnectionManager(){}

    public static Connection getConnection(){
        if(connection == null){
            try{
                LOGGER.fine("Getting connection");
                loadProperties();
                connection = DriverManager.getConnection(url, username, password);
            }
            catch(SQLException e){
                LOGGER.severe("Driver failed" + e.getMessage());
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.severe("Failed to close connection: "+ e.getMessage());
                LOGGER.warning("Setting connection to null");
                connection = null;
            }
        }
    }

    private static void loadProperties(){
        LOGGER.fine("Loading properties from file...");
        Properties properties = new Properties();
        try(FileReader reader = new FileReader("src/main/resources/database.properties")){
            properties.load(reader);
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
        } catch (IOException e) {
            LOGGER.severe("Failed to load database properties: "+ e.getMessage());
        }
        LOGGER.fine("Properties successfully loaded.");
    }
        }
    }
}
