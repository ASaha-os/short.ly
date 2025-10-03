package com.yourcompany.urlshortener;

public class UrlShortenerApp {

    public static void main(String[] args) {
        System.out.println("--- URL Shortener App Starting ---");
        
        // --- PR 3: Test Database Connection ---
        // This should print "successful!" if the setup is right.
        DatabaseManager.testConnection();
        // --------------------------------------
        
        // --- PR 2 Test (Retained) ---
        String newKey = KeyGenerator.generateKey(6);
        System.out.println("Test key generated: " + newKey);
        // -----------------------------
        
        // PR 7 will implement the full interactive application loop here!
    }
}