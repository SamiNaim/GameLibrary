package org.example.Database;

import org.example.core.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.example.core.Config.*;
/**
 * Service class for managing games in the database.
 */
public class GameService {

    /**
     * Adds a new game to the database.
     */
    
    public static void addGame(String title, String developer, Genre genre, double price, int copies, int subscription) {
        String sql = "INSERT INTO games (title, developer, genre, price, copies, subscription) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, developer);
            stmt.setString(3, genre.toString());
            stmt.setDouble(4, price);
            stmt.setInt(5, copies);
            stmt.setInt(6, subscription);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a game from the database by title.
     */

    public static void removeGame(String title) {
        String sql = "DELETE FROM games WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a game exists in the database.
     */
    
    public static boolean gameExists(String game) {
        String sql = "SELECT 1 FROM games WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, game);
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks if a game has available copies.
     */

    public static boolean gameAvailable(String game) {
        String sql = "SELECT copies FROM games WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, game);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int copies = rs.getInt("copies");
                return copies > 0;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Decreases the number of copies of a game by one.
     */

    public static void decreaseCopies(String game) {
        String sql = "UPDATE games SET copies = copies - 1 WHERE title = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, game);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Checks if a game is available in a subscription.
     */

    public static boolean gameInSubscription(String game) {
        String sql = "SELECT subscription FROM games WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, game);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("subscription") == 1;
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the price of a game.
     */

    public static double getGamePrice(String game) {
        String sql = "SELECT price FROM games WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, game);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("price");
            }

            return 0.0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * Retrieves a list of all games from the database.
     */

    public static List<String> getAllGames() {
        List<String> games = new ArrayList<>();
        String sql = "SELECT title, developer, genre, price, copies, subscription FROM games";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String developer = rs.getString("developer");
                String genre = rs.getString("genre");
                double price = rs.getDouble("price");
                int copies = rs.getInt("copies");
                int subscription = rs.getInt("subscription");

                String gameInfo = String.format("%s - %s - %s - %.2f€ - %d copies",
                        title, developer, genre, price, copies);

                if (subscription == 1) {
                    gameInfo += " - available in subscription";
                }

                games.add(gameInfo);
            }

            return games;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves a list of games that are available in the subscription service.
     *
     * @return A list of strings representing subscription games with their title, developer, and genre.
     */

    public static List<String> getSubscriptionGames() {
        List<String> games = new ArrayList<>();
        String sql = "SELECT title, developer, genre, price, copies FROM games WHERE subscription = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                String developer = rs.getString("developer");
                String genre = rs.getString("genre");

                String gameDetails = String.format("%s - %s - %s", title, developer, genre);
                games.add(gameDetails);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }

    /**
     * Updates the subscription status of a game.
     *
     * @param title      The title of the game.
     * @param subscribed {@code true} to enable subscription, {@code false} to disable it.
     */

    public static void setGameSubscription(String title, boolean subscribed) {
        String sql = "UPDATE games SET subscription = ? WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, subscribed ? 1 : 0);
            stmt.setString(2, title);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the price of a game.
     *
     * @param title    The title of the game.
     * @param newPrice The new price of the game.
     */

    public static void changeGamePrice(String title, double newPrice) {
        String sql = "UPDATE games SET price = ? WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, newPrice);
            stmt.setString(2, title);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cleanUp() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("PRAGMA foreign_keys = OFF");
            stmt.executeUpdate("DELETE FROM games");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='games'");
            stmt.executeUpdate("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
