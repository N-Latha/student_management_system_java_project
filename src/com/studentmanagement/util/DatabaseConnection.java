package com.studentmanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class for establishing and managing MySQL database connections via JDBC.
 * Contains centralized database credentials.
 */
public class DatabaseConnection {

    // =========================================================================
    // DATABASE CONFIGURATION (Change these credentials according to your MySQL)
    // =========================================================================
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USERNAME = "root";       // Change if your MySQL user is different
    private static final String DB_PASSWORD = "Latha@12345"; // MySQL password
    // =========================================================================

    // Static block to load MySQL JDBC Driver once when the class is initialized
    static {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("[DatabaseConnection] MySQL JDBC Driver registered successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("[DatabaseConnection] ERROR: MySQL JDBC Driver not found! Make sure mysql-connector-j.jar is in WEB-INF/lib.");
            e.printStackTrace();
        }
    }

    /**
     * Obtains a new active connection to the MySQL database.
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("[DatabaseConnection] ERROR: Failed to connect to MySQL database at " + DB_URL);
            System.err.println("Verify MySQL service is running and username/password are correct.");
            throw e;
        }
    }

    /**
     * Helper method to safely close JDBC resources (Connection, Statement, ResultSet).
     * Prevents resource leaks in application.
     */
    public static void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
