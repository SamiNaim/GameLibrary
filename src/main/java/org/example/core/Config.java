package org.example.core;
/**
 * Configuration class for storing application-wide constants.
 * This class contains database connection details and subscription pricing.
 */

public class Config {

    /**
     *
     * Database URL for the SQLite database.
     * The currently active configuration points to a test database.
     * To use the main database, uncomment the second DB_URL and comment out the first.
     *
     */

    //public static final String DB_URL = "jdbc:sqlite:src/test/java/gameLibrary.db";

    /**
     * Alternative database URL for production use.
     */
    public static final String DB_URL = "jdbc:sqlite:src/main/resources/gameLibrary.db";

    /**
     * The price of a subscription in the application.
     */
    public static final double SUBSCRIPTION_PRICE = 29.99;
}
