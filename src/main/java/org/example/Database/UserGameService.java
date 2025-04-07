package org.example.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.core.Config.DB_URL;
/**
 * Provides database operations for managing the relationship between users and games.
 */
public class UserGameService {

    /**
     * Checks if a user owns a specific game, either directly or via subscription.
     *
     * @param userID The ID of the user.
     * @param game   The title of the game.
     * @return {@code true} if the user owns the game, {@code false} otherwise.
     */

    public static boolean gameOwned(int userID, String game) {
        String gameIdQuery = "SELECT id, subscription FROM games WHERE title = ?";
        String ownedQuery = "SELECT 1 FROM user_games WHERE user_id = ? AND game_id = ?";
        String userSubscriptionQuery = "SELECT subscribed FROM users WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            int gameId = -1;
            boolean isSubscription = false;

            try (PreparedStatement stmt = conn.prepareStatement(gameIdQuery)) {
                stmt.setString(1, game);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    gameId = rs.getInt("id");
                    isSubscription = rs.getInt("subscription") == 1;
                } else {
                    return false;
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(ownedQuery)) {
                stmt.setInt(1, userID);
                stmt.setInt(2, gameId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return true;
                }
            }

            if (isSubscription) {
                try (PreparedStatement stmt = conn.prepareStatement(userSubscriptionQuery)) {
                    stmt.setInt(1, userID);
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        return rs.getInt("subscribed") == 1;
                    }
                }
            }

            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves a list of games owned by a specific user.
     *
     * @param userID The ID of the user.
     * @return A list of game titles, along with developer and genre information.
     */

    public static List<String> getUserGames(int userID) {
        List<String> games = new ArrayList<>();
        String sql = "SELECT g.title, g.developer, g.genre, g.price, g.copies " +
                "FROM user_games ug " +
                "JOIN games g ON ug.game_id = g.id " +
                "WHERE ug.user_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);

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
     * Adds a game to a user's collection.
     *
     * @param game   The title of the game.
     * @param userID The ID of the user.
     */

    public static void addGameToUser(String game, int userID) {
        String sql = "SELECT id FROM games WHERE title = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, game);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int gameID = rs.getInt("id");
                String insertGameSql = "INSERT INTO user_games (user_id, game_id) VALUES (?, ?)";

                try (PreparedStatement insertStmt = conn.prepareStatement(insertGameSql)) {
                    insertStmt.setInt(1, userID);
                    insertStmt.setInt(2, gameID);
                    insertStmt.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cleans up all user-game related data in the database.
     * This method also cleans up user accounts and game data.
     */

    public static void cleanUp() {
        AccountService.cleanUp();
        GameService.cleanUp();
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("PRAGMA foreign_keys = OFF");
            stmt.executeUpdate("DELETE FROM user_games");
            stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='user_games'");
            stmt.executeUpdate("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
