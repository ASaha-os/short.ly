package com.yourcompany.urlshortener;

import java.sql.*;

/**
 * PR 9: Handles database operations, including improved error checking 
 * to specifically detect unique key constraint violations (collisions).
 */
public class DatabaseManager {

    private static final String DB_URL = "jdbc:h2:mem:shortenerdb";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = ""; 

    private static final String CREATE_TABLE_SQL = 
        "CREATE TABLE IF NOT EXISTS urls (" +
        "    id INT AUTO_INCREMENT PRIMARY KEY," +
        "    short_key VARCHAR(10) NOT NULL UNIQUE," +
        "    long_url VARCHAR(2048) NOT NULL," +
        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
        ");";
    
    // SQL for saving a URL mapping
    private static final String INSERT_URL_SQL = 
        "INSERT INTO urls (short_key, long_url) VALUES (?, ?)";

    // SQL for finding the original URL
    private static final String SELECT_URL_SQL = 
        "SELECT long_url FROM urls WHERE short_key = ?";

    // PR 9: Unique Constraint Violation Code (for H2 Database)
    private static final String UNIQUE_CONSTRAINT_ERROR_CODE = "23505";


    public static Connection getConnection() {
        try {
            Class.forName("org.h2.Driver"); 
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: H2 JDBC Driver not found.");
            return null;
        } catch (SQLException e) {
            System.err.println("❌ Error connecting to the database. Check settings.");
            e.printStackTrace();
            return null;
        }
    }
    
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            if (conn != null) {
                stmt.execute(CREATE_TABLE_SQL);
                System.out.println("✅ Schema status: 'urls' table initialized successfully.");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error initializing database table: " + e.getMessage());
        }
    }

    /**
     * PR 9: Saves the URL mapping. Returns true on success, false on failure (including collision).
     */
    public static boolean saveUrl(String shortKey, String longUrl) {
        // Use try-with-resources to ensure PreparedStatement and Connection are closed
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_URL_SQL)) {
            
            pstmt.setString(1, shortKey);
            pstmt.setString(2, longUrl);
            
            int rowsAffected = pstmt.executeUpdate();
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            // PR 9: Check specifically for Unique Constraint Violation (Key Collision)
            if (e.getSQLState().equals(UNIQUE_CONSTRAINT_ERROR_CODE)) {
                System.err.println("⚠️ Database Error: Key collision detected for key '" + shortKey + "'. Retrying...");
                // Return false so the application layer knows to try again.
            } else {
                System.err.println("❌ Database Error during save operation: " + e.getMessage());
            }
            return false;
        }
    }
    
    // ... (findLongUrl method remains the same)
    public static String findLongUrl(String shortKey) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_URL_SQL)) {
            
            pstmt.setString(1, shortKey);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("long_url");
                }
            }
            return null;
            
        } catch (SQLException e) {
            System.err.println("❌ Database Error during lookup: " + e.getMessage());
            return null;
        }
    }
}
