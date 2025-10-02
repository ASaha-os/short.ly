

import java.util.Random;

/**
 * PR 2: Implements the core logic for generating short, unique-looking keys.
 * This class is responsible for creating the random alphanumeric strings
 * that will be used as the shortened URL identifiers.
 */
public class KeyGenerator {

    // Define the set of characters allowed in the short key (alphanumeric: A-Z, a-z, 0-9)
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    
    // We use a simple Random instance for efficient key generation.
    private static final Random RANDOM = new Random();

    /**
     * Generates a random alphanumeric key of a specified length.
     * * We'll stick to a length of 6 characters for our mini-shortener.
     * * @param length The desired length of the short key (e.g., 6).
     * @return A randomly generated alphanumeric string.
     */
    public static String generateKey(int length) {
        // StringBuilder is the fastest way to build strings in a loop
        StringBuilder builder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // Pick a random index from the ALPHABET string
            int randomIndex = RANDOM.nextInt(ALPHABET.length());
            
            // Append the character at that random index to our key
            builder.append(ALPHABET.charAt(randomIndex));
        }

        return builder.toString();
    }
    
    // ----------------------------------------------------------------------
    // Note: The main method below is just for quick testing!
    // ----------------------------------------------------------------------
    public static void main(String[] args) {
        int keyLength = 6;
        System.out.println("--- Key Generator Test ---");
        System.out.println("Generated Key 1: " + generateKey(keyLength));
        System.out.println("Generated Key 2: " + generateKey(keyLength));
        System.out.println("Generated Key (Length 8): " + generateKey(8));
    }
}
