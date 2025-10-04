package com.yourcompany.urlshortener;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * PR 5 & PR 6: Handles database connection, table creation, saving (shortening), 
 * and looking up (retrieving) URLs.
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

    // PR 5: SQL statement for inserting a new URL pair
    private static final String INSERT_URL_SQL = 
        "INSERT INTO urls (short_key, long_url) VALUES (?, ?)";

    // PR 6: SQL statement for looking up the original URL by key
    private static final String SELECT_URL_SQL =
        "SELECT long_url FROM urls WHERE short_key = ?";


    public static Connection getConnection() {
        try {
            Class.forName("org.h2.Driver"); 
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
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
     * PR 5 Feature: Saves the short key and original URL to the database.
     * @param shortKey The unique key generated for the URL.
     * @param longUrl The original long URL.
     * @return true if the save was successful, false otherwise.
     */
    public static boolean saveUrl(String shortKey, String longUrl) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_URL_SQL)) {

            pstmt.setString(1, shortKey); 
            pstmt.setString(2, longUrl); 

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            // This is where we'll focus on collision errors later (PR 9)
            System.err.println("❌ Database save failed for key '" + shortKey + "': " + e.getMessage());
            return false;
        }
    }

    /**
     * PR 6 Feature: Retrieves the original long URL associated with a short key.
     * @param shortKey The key to look up.
     * @return The original long URL, or null if the key is not found.
     */
    public static String findLongUrl(String shortKey) {
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_URL_SQL)) {

            pstmt.setString(1, shortKey); 

            try (ResultSet rs = pstmt.executeQuery()) {
                
                if (rs.next()) {
                    // Get the 'long_url' value from the result row
                    return rs.getString("long_url");
                }
            }
            
            // If the key wasn't found, return null
            return null;

        } catch (SQLException e) {
            System.err.println("❌ Database lookup failed for key '" + shortKey + "': " + e.getMessage());
            return null;
        }
    }

    /**
     * Test method updated to run initialization, save, and then lookup.
     */
    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ Connection status: Database connection successful.");
                initializeDatabase(); 
                
                // --- PR 5/6 Combined Test ---
                String testKey = KeyGenerator.generateKey(6);
                String testUrl = "https://www.google.com/mega-long-url-for-pr5-pr6-test";
                
                // 1. Save the URL (PR 5)
                if (saveUrl(testKey, testUrl)) {
                    System.out.println("✅ Save status: Key '" + testKey + "' stored successfully.");
                    
                    // 2. Look up the key we just saved (PR 6)
                    String retrievedUrl = findLongUrl(testKey);
                    
                    if (testUrl.equals(retrievedUrl)) {
                        System.out.println("✅ Lookup status: Success! Retrieved URL matches saved URL. (PR 5 & 6 PASS)");
                        System.out.println("   Key: " + testKey + " -> URL: " + retrievedUrl);
                    } else {
                        System.err.println("❌ Lookup status: Retrieved URL did not match saved URL! (PR 5 & 6 FAIL)");
                    }
                    
                } else {
                    System.err.println("❌ Save status: Failed to store test key.");
                }

            } else {
                System.err.println("❌ Connection status: Database connection failed!");
            }
        } catch (SQLException e) {
            System.err.println("Error checking connection status: " + e.getMessage());
        }
    }
}