package com.example.testrepo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    // Database URL, username, and password
    private static final String URL = "jdbc:mariadb://localhost:3306/CMS";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    // Get the database connection
    public static Connection getConnection() {
        Connection con = null;
        try {
            con= DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
        }
        return con;
    }
}