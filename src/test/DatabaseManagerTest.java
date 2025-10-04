package com.yourcompany.urlshortener;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PR 7: Integration tests for the DatabaseManager (Save and Lookup operations).
 * This uses the in-memory H2 database, which is initialized and destroyed 
 * for each test class run.
 */
public class DatabaseManagerTest {
    
    private static final String LONG_URL = "https://www.example.com/very/long/path/to/page";
    private static final String SHORT_KEY = "TestKey1";
    private static final String UNIQUE_SHORT_KEY = "Unique2";

    /**
     * Ensures the database schema is created before any tests run.
     * This calls the logic from PR 4.
     */
    @BeforeAll
    static void setupDatabase() {
        DatabaseManager.initializeDatabase();
        // Database is now ready with an empty 'urls' table
    }

    /**
     * Clears the table before each test to ensure tests are independent.
     */
    @BeforeEach
    @AfterEach 
    void cleanupDatabase() {
        try (java.sql.Connection conn = DatabaseManager.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // Deletes all rows in the 'urls' table
            stmt.execute("DELETE FROM urls");
        } catch (java.sql.SQLException e) {
            System.err.println("Cleanup failed: " + e.getMessage());
        }
    }

    /**
     * Test 1: Verifies successful saving of a URL (PR 5).
     */
    @Test
    void saveUrl_shouldReturnTrueAndStoreData() {
        assertTrue(DatabaseManager.saveUrl(SHORT_KEY, LONG_URL), 
            "Save operation should return true on success.");
    }

    /**
     * Test 2: Verifies successful lookup of a previously saved URL (PR 6).
     */
    @Test
    void findLongUrl_shouldRetrieveCorrectUrl() {
        // Arrange: Save a URL first
        DatabaseManager.saveUrl(SHORT_KEY, LONG_URL);
        
        // Act: Look it up
        String retrievedUrl = DatabaseManager.findLongUrl(SHORT_KEY);
        
        // Assert
        assertNotNull(retrievedUrl, "Retrieved URL should not be null.");
        assertEquals(LONG_URL, retrievedUrl, "Retrieved URL must match the original URL.");
    }

    /**
     * Test 3: Verifies that an unknown key returns null (PR 6).
     */
    @Test
    void findLongUrl_shouldReturnNullForUnknownKey() {
        // Act: Look up a key that was never saved
        String retrievedUrl = DatabaseManager.findLongUrl("MISSING");
        
        // Assert
        assertNull(retrievedUrl, "Lookup should return null for non-existent key.");
    }

    /**
     * Test 4: Verifies that saving a duplicate key fails (Database constraint check).
     */
    @Test
    void saveUrl_shouldFailOnDuplicateKey() {
        // Arrange: Save the key successfully once
        assertTrue(DatabaseManager.saveUrl(UNIQUE_SHORT_KEY, LONG_URL + "1"));
        
        // Act: Try to save the exact same key again
        assertFalse(DatabaseManager.saveUrl(UNIQUE_SHORT_KEY, LONG_URL + "2"), 
            "Saving the same key again must fail due to UNIQUE constraint.");
    }
}