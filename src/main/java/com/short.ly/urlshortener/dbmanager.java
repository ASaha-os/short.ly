package com.yourcompany.urlshortener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * PR 4: Handles the connection to the H2 database and ensures the table structure exists.
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:h2:mem:shortenerdb";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = ""; 

    // SQL statement to create the 'urls' table
    private static final String CREATE_TABLE_SQL = 
        "CREATE TABLE IF NOT EXISTS urls (" +
        "    id INT AUTO_INCREMENT PRIMARY KEY," +
        "    short_key VARCHAR(10) NOT NULL UNIQUE," +
        "    long_url VARCHAR(2048) NOT NULL," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");";

    /**
     * Tries to establish and return a new connection to the H2 database.
     * (No changes here from PR 3)
     */
    public static Connection getConnection() {
        try {
            Class.forName("org.h2.Driver"); 
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: H2 JDBC Driver not found. Did you check pom.xml?");
            return null;
        } catch (SQLException e) {
            System.err.println("❌ Error connecting to the database. Check settings.");
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * PR 4 Feature: Creates the 'urls' table if it doesn't already exist.
     */
    public static void initializeDatabase() {
        // We use try-with-resources to automatically close the Connection and Statement
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            if (conn != null) {
                // Execute the CREATE TABLE SQL command
                stmt.execute(CREATE_TABLE_SQL);
                System.out.println("✅ Schema status: 'urls' table initialized successfully. (PR 4 PASS)");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error initializing database table: " + e.getMessage());
        }
    }

    /**
     * Simple test method updated to run initialization.
     */
    public static void testConnection() {
        // Test connection first, then initialize the table
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Connection status: Database connection successful.");
                initializeDatabase(); // <-- Calls the new feature!
            } else {
                System.err.println("❌ Connection status: Database connection failed!");
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
        }
    }
}