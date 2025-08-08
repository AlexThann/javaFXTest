package com.example.testrepo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    // Database URL, username, and password
    private static final String URL = "jdbc:mariadb://localhost:3306/CMS";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    // Get the database connection
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Create the connection
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println(" Connected to MariaDB database!");
            } catch (SQLException e) {
                System.err.println(" Connection failed! Check DB settings.");
            }
        }
        return connection;
    }

    // Close the connection
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.out.println("Failed to close connection.");
            }
        }
    }
}