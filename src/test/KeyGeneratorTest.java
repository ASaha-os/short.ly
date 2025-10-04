package com.yourcompany.urlshortener;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PR 7: Unit tests for the KeyGenerator logic.
 * These tests ensure the generated keys are the correct length, 
 * use the correct characters, and are highly unique.
 */
public class KeyGeneratorTest {
    
    /**
     * Test: Ensures keys are generated with the correct length.
     */
    @Test
    void generatedKey_shouldHaveCorrectLength() {
        int expectedLength = 8;
        String key = KeyGenerator.generateKey(expectedLength);
        assertEquals(expectedLength, key.length(), "Key length must match requested length.");
    }

    /**
     * Test: Ensures keys generated are unique across multiple runs.
     * Note: We test 1000 keys to ensure the high probability of uniqueness.
     */
    @Test
    void generatedKeys_shouldBeUnique() {
        // Generate 1000 keys and ensure the Set size is 1000
        int count = 1000;
        java.util.Set<String> keys = new java.util.HashSet<>();
        
        for (int i = 0; i < count; i++) {
            keys.add(KeyGenerator.generateKey(6));
        }
        
        assertEquals(count, keys.size(), "All generated keys must be unique.");
    }
    
    /**
     * Test: Ensures the generated key only contains valid alphanumeric characters (A-Z, a-z, 0-9).
     */
    @Test
    void generatedKey_shouldContainOnlyAlphanumericCharacters() {
        String key = KeyGenerator.generateKey(10);
        // Uses a Regular Expression to check for alphanumeric characters only
        assertTrue(key.matches("^[a-zA-Z0-9]+$"), "Key must be strictly alphanumeric.");
    }
}
