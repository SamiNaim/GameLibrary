import org.example.Database.GameService;
import org.example.core.Genre;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    @BeforeAll
    public static void setup() {
        String title = "The First Berserker";
        String dev = "Neople";
        double price = 59.99;
        int copies = 100;
        int subscription = 1;
        GameService.addGame(title, dev, Genre.ACTION, price, copies, subscription);
    }

    @AfterAll
    public static void cleanUp() {
        GameService.cleanUp();
    }

    @Test
    public void testGameExists_ReturnsTrue_WhenGameIsFound() {
        // Given the game "The First Berserker" exists in the database
        String gameTitle = "The First Berserker";

        // When the system checks if the game exists
        boolean result = GameService.gameExists(gameTitle);

        // Then it returns true
        assertTrue(result, "The game should exist in the database.");
    }

    @Test
    public void testGameExists_ReturnsFalse_WhenGameIsNotFound() {
        // Given the game "Nonexistent Game" does not exist in the database
        String gameTitle = "Nonexistent Game";

        // When the system checks if the game exists
        boolean result = GameService.gameExists(gameTitle);

        // Then it returns false
        assertFalse(result, "The game should not exist in the database.");
    }

    @Test
    public void testGameInSubscription_ReturnsTrue_WhenGameIsSubscribed() {
        // Given the game "The First Berserker" is marked as available in the subscription
        String gameTitle = "The First Berserker";

        // When the system checks if the game is in the subscription
        boolean result = GameService.gameInSubscription(gameTitle);

        // Then it returns true
        assertTrue(result, "The game should be available in subscription.");
    }

    @Test
    public void testGameInSubscription_ReturnsFalse_WhenGameIsNotSubscribed() {
        // Given the game "The Second Berserker" is not available in the subscription
        GameService.addGame("The Second Berserker", "Meople", Genre.ACTION, 49.99, 80, 0); // Resetting subscription to 0
        String gameTitle = "The Second Berserker";

        // When the system checks if the game is in the subscription
        boolean result = GameService.gameInSubscription(gameTitle);

        // Then it returns false
        assertFalse(result, "The game should not be available in subscription.");
    }

    @Test
    public void testGetGamePrice_ReturnsCorrectPrice() {
        // Given the game "The First Berserker" has a price of 59.99
        String gameTitle = "The First Berserker";

        // When the system retrieves the price of the game
        double price = GameService.getGamePrice(gameTitle);

        // Then it returns 59.99
        assertEquals(59.99, price, "The price of the game should be correct.");
    }

    @Test
    public void testGetAllGames_ReturnsListOfGames() {
        // Given there are games stored in the database

        // When the system retrieves all games
        List<String> games = GameService.getAllGames();

        // Then it returns a non-empty list of game titles
        assertFalse(games.isEmpty(), "The list of games should not be empty.");
    }

    @Test
    public void testGetSubscriptionGames_ReturnsListOfSubscriptionGames() {
        // Given the game "The First Berserker" is available in the subscription

        // When the system retrieves all subscription games
        List<String> subscriptionGames = GameService.getSubscriptionGames();

        // Then the list contains "The First Berserker by Neople - action"
        assertTrue(subscriptionGames.contains("The First Berserker by Neople - action"), "The game should be listed in subscription games.");
    }

    @Test
    public void testSetGameSubscription_UpdatesSubscriptionCorrectly() {
        // Given the game "The First Berserker" is available in the subscription
        String gameTitle = "The First Berserker";

        // When the system sets the game's subscription status to false
        GameService.setGameSubscription(gameTitle, false);
        boolean result = GameService.gameInSubscription(gameTitle);

        // Then the game is no longer available in the subscription
        assertFalse(result, "The game's subscription status should be updated correctly.");
    }

    @Test
    public void testChangeGamePrice_UpdatesPriceCorrectly() {
        // Given the game "The First Berserker" exists in the database
        String gameTitle = "The First Berserker";
        double newPrice = 49.99;

        // When the system updates the game's price to 49.99
        GameService.changeGamePrice(gameTitle, newPrice);
        double updatedPrice = GameService.getGamePrice(gameTitle);

        // Then retrieving the price of the game returns 49.99
        assertEquals(newPrice, updatedPrice, "The price should be updated correctly.");
    }
}
