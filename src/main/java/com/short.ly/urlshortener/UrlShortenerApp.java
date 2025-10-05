package com.yourcompany.urlshortener;

import java.util.Scanner;

/**
 * PR 9: Main application class. Adds retry logic for key collisions.
 */
public class UrlShortenerApp {

    private final Scanner scanner = new Scanner(System.in);
    private final int KEY_LENGTH = 6;
    // PR 9: Define the maximum number of times to try saving a key
    private final int MAX_RETRIES = 5; 
    
    private boolean isDatabaseReady = false;

    public static void main(String[] args) {
        UrlShortenerApp app = new UrlShortenerApp();
        app.start();
    }
    
    public void start() {
        System.out.println("--- URL Shortener Console App v1.0 (Robust) ---");
        
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
    
    private static boolean isValidUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }

    /**
     * PR 9: Implements the retry loop for shortening a URL.
     */
    private void shortenUrl() {
        System.out.print("\nEnter the URL to shorten: ");
        String longUrl = scanner.nextLine().trim();
        
        if (longUrl.isEmpty()) {
            System.out.println("❗ URL cannot be empty.");
            return;
        }
        
        if (!isValidUrl(longUrl)) {
            System.out.println("❌ Invalid URL format. URL must start with http:// or https://");
            return;
        }
        
        // PR 9: Retry Logic Start
        for (int retry = 1; retry <= MAX_RETRIES; retry++) {
            String shortKey = KeyGenerator.generateKey(KEY_LENGTH);
            
            if (DatabaseManager.saveUrl(shortKey, longUrl)) {
                // Success path
                System.out.println("\n✅ Success! Your shortened URL key is:");
                System.out.println("   --> Key: " + shortKey);
                return; // Exit the function after success
            }
            
            // Failure path (likely collision, handled in DatabaseManager)
            if (retry < MAX_RETRIES) {
                // If it fails but we have retries left, wait briefly before trying again (optional, but good practice)
                System.out.println("   Attempt " + retry + " failed. Retrying with a new key...");
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
        }
        
        // If the loop finishes without returning (meaning MAX_RETRIES was reached)
        System.out.println("\n❌ FAILED! Could not shorten URL after " + MAX_RETRIES + " attempts due to repeated collisions. Try again later.");
    }
    
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
