public class DatabaseManager {
    
}
package com.yourcompany.urlshortener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * PR 3: Handles the connection to the H2 database using JDBC.
 */
public class DatabaseManager {

    // These constants match the config in application.properties
    private static final String DB_URL = "jdbc:h2:mem:shortenerdb";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = ""; 

    /**
     * Tries to establish and return a new connection to the H2 database.
     * @return A valid Connection object, or null if connection fails.
     */
    public static Connection getConnection() {
        try {
            // Load the H2 Driver (optional since Java 6, but good for robustness)
            Class.forName("org.h2.Driver"); 
            
            // Connect using the driver manager
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
     * Simple test method to confirm the connection works for PR 3.
     */
    public static void testConnection() {
        // 'try-with-resources' automatically closes the connection for us.
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Connection status: Database connection successful! (PR 3 PASS)");
            } else {
                System.err.println("❌ Connection status: Database connection failed! (PR 3 FAIL)");
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
        }
    }
}