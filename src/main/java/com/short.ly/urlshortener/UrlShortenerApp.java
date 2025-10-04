package com.yourcompany.urlshortener;

import java.util.Scanner;

/**
 * PR 8: Main application class. Adds basic URL validation logic.
 */
public class UrlShortenerApp {

    private final Scanner scanner = new Scanner(System.in);
    private final int KEY_LENGTH = 6;
    
    private boolean isDatabaseReady = false;

    public static void main(String[] args) {
        UrlShortenerApp app = new UrlShortenerApp();
        app.start();
    }
    
    /**
     * Initializes the database connection and starts the main application loop.
     */
    public void start() {
        System.out.println("--- URL Shortener Console App v1.0 ---");
        
        // 1. Initialize and confirm DB connection and schema
        if (DatabaseManager.getConnection() != null) {
            DatabaseManager.initializeDatabase(); 
            isDatabaseReady = true;
        } else {
            System.err.println("CRITICAL: Database connection failed. Cannot start application.");
            return;
        }
        
        // 2. Start the interactive loop
        mainLoop();
    }

    /**
     * Presents the user menu and handles command input.
     */
    private void mainLoop() {
        String input;
        
        if (!isDatabaseReady) return; 

        while (true) {
            System.out.println("\n-------------------------------------");
            System.out.println("Select an option:");
            System.out.println(" [1] Shorten a long URL");
            System.out.println(" [2] Lookup a short key");
            System.out.println(" [0] Exit");
            System.out.print(" >> ");

            input = scanner.nextLine().trim();
            
            switch (input) {
                case "1":
                    shortenUrl();
                    break;
                case "2":
                    lookupUrl();
                    break;
                case "0":
                    System.out.println("\nGoodbye! Thanks for using the URL Shortener.");
                    return; 
                default:
                    System.out.println("❗ Invalid choice. Please enter 1, 2, or 0.");
            }
        }
    }
    
    /**
     * Checks if the URL has a valid protocol prefix.
     * This is a simple validation (PR 8).
     */
    private static boolean isValidUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }

    /**
     * Prompts for a long URL, validates it, generates a key, and saves it.
     */
    private void shortenUrl() {
        System.out.print("\nEnter the URL to shorten: ");
        String longUrl = scanner.nextLine().trim();
        
        if (longUrl.isEmpty()) {
            System.out.println("❗ URL cannot be empty.");
            return;
        }
        
        // PR 8: URL Validation Check
        if (!isValidUrl(longUrl)) {
            System.out.println("❌ Invalid URL format. URL must start with http:// or https://");
            return;
        }
        
        String shortKey = KeyGenerator.generateKey(KEY_LENGTH);
        
        if (DatabaseManager.saveUrl(shortKey, longUrl)) {
            System.out.println("\n✅ Success! Your shortened URL key is:");
            System.out.println("   --> Key: " + shortKey);
        } else {
            System.out.println("❌ Failed to shorten URL. Database error occurred.");
        }
    }
    
    /**
     * Prompts for a short key and retrieves the original URL.
     */
    private void lookupUrl() {
        System.print("\nEnter the short key to lookup: ");
        String shortKey = scanner.nextLine().trim();

        if (shortKey.isEmpty()) {
            System.out.println("❗ Key cannot be empty.");
            return;
        }
        
        String longUrl = DatabaseManager.findLongUrl(shortKey);
        
        if (longUrl != null) {
            System.out.println("\n✅ Key found! Original URL:");
            System.out.println("   --> URL: " + longUrl);
        } else {
            System.out.println("\n❗ Key not found in the database. Try shortening a URL first!");
        }
    }
}
