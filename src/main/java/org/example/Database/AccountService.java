package org.example.Database;

import java.sql.*;

import static org.example.core.Config.*;

/**
 * The {@code AccountService} class provides methods for managing user accounts
 * in the database, including authentication, registration, balance management,
 * and subscription status.
 */
public class AccountService {

    /**
     * Checks if an email is already registered in the database.
     *
     * @param email The email address to check.
     * @return {@code true} if the email exists, {@code false} otherwise.
     */

    public static boolean emailExists(String email) {
        email = email.toLowerCase();
        String sql = "SELECT COUNt(*) FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifies if the provided password matches the stored password for the given email.
     *
     * @param email    The user's email address.
     * @param password The password to verify.
     * @return {@code true} if the password matches, {@code false} otherwise.
     */

    public static boolean verifyPassword(String email, String password) {
        email = email.toLowerCase();
        String sql = "SELECT password FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return storedPassword.equals(password);
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a user has staff privileges.
     *
     * @param userID The user's ID.
     * @return {@code true} if the user is staff, {@code false} otherwise.
     */

    public static boolean isStaff(int userID) {
        String sql = "SELECT staff FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int staff = rs.getInt("staff");
                return staff == 1;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Registers a new user in the database.
     *
     * @param email    The user's email address.
     * @param password The user's password.
     * @param name     The user's name.
     * @param phoneNo  The user's phone number.
     * @return {@code true} if the registration was successful, {@code false} otherwise.
     */

    public static boolean registerUser(String email, String password, String name, String phoneNo) {
        email = email.toLowerCase();
        String sql = "INSERT INTO users (email, password, name, phoneNo, balance, staff, subscribed) VALUES (?, ?, ?, ?, 0.0, 0, 0)";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, phoneNo);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the user ID based on the email address.
     *
     * @param email The user's email address.
     * @return The user ID, or -1 if not found.
     */

    public static int getUserID(String email) {
        email = email.toLowerCase();
        String sql = "SELECT id FROM users WHERE email = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            int id = -1;
            if (rs.next()) {
                id = rs.getInt("id");
            }

            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

    }

    /**
     * Retrieves the current balance of a user.
     *
     * @param userID The user's ID.
     * @return The balance of the user.
     */

    public static double getUserBalance(int userID) {
        String sql = "SELECT balance FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Updates the balance of a user.
     *
     * @param userID  The user's ID.
     * @param balance The new balance amount.
     */

    public static void setUserBalance(int userID, double balance) {
        String sql = "UPDATE users SET balance = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, balance);
            stmt.setInt(2, userID);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a user is subscribed.
     *
     * @param userID The user's ID.
     * @return {@code true} if the user is subscribed, {@code false} otherwise.
     */

    public static boolean isSubscribed(int userID) {
        String sql = "SELECT subscribed FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("subscribed") == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates the subscription status of a user.
     *
     * @param userID     The user's ID.
     * @param subscribed {@code true} to subscribe, {@code false} to unsubscribe.
     */

    public static void setUserSubscribed(int userID, boolean subscribed) {
        String sql = "UPDATE users SET subscribed = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subscribed ? 1 : 0);
            stmt.setInt(2, userID);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cleans up the database by removing all users and resetting the user table's ID sequence.
     * This method is typically used for testing or resetting the database.
     */

    public static void cleanUp() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("PRAGMA foreign_keys = OFF");
            stmt.executeUpdate("DELETE FROM users");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='users'");
            stmt.executeUpdate("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
