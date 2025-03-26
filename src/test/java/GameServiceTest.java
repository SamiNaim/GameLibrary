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
        // Given
        String gameTitle = "The First Berserker";

        // When
        boolean result = GameService.gameExists(gameTitle);

        // Then
        assertTrue(result, "The game should exist in the database.");
    }

    @Test
    public void testGameExists_ReturnsFalse_WhenGameIsNotFound() {
        // Given
        String gameTitle = "Nonexistent Game";

        // When
        boolean result = GameService.gameExists(gameTitle);

        // Then
        assertFalse(result, "The game should not exist in the database.");
    }

    @Test
    public void testGameInSubscription_ReturnsTrue_WhenGameIsSubscribed() {
        // Given
        String gameTitle = "The First Berserker";

        // When
        boolean result = GameService.gameInSubscription(gameTitle);

        // Then
        assertTrue(result, "The game should be available in subscription.");
    }

    @Test
    public void testGameInSubscription_ReturnsFalse_WhenGameIsNotSubscribed() {
        // Given
        GameService.addGame("The Second Berserker", "Meople", Genre.ACTION, 49.99, 80, 0); // Resetting subscription to 0
        String gameTitle = "The Second Berserker";

        // When
        boolean result = GameService.gameInSubscription(gameTitle);

        // Then
        assertFalse(result, "The game should not be available in subscription.");
    }

    @Test
    public void testGetGamePrice_ReturnsCorrectPrice() {
        // Given
        String gameTitle = "The First Berserker";

        // When
        double price = GameService.getGamePrice(gameTitle);

        // Then
        assertEquals(59.99, price, "The price of the game should be correct.");
    }

    @Test
    public void testGetAllGames_ReturnsListOfGames() {
        // When
        List<String> games = GameService.getAllGames();

        // Then
        assertFalse(games.isEmpty(), "The list of games should not be empty.");
    }

    @Test
    public void testGetSubscriptionGames_ReturnsListOfSubscriptionGames() {
        // When
        List<String> subscriptionGames = GameService.getSubscriptionGames();

        // Then
        assertTrue(subscriptionGames.contains("The First Berserker by Neople - action"), "The game should be listed in subscription games.");
    }

    @Test
    public void testSetGameSubscription_UpdatesSubscriptionCorrectly() {
        // Given
        String gameTitle = "The First Berserker";

        // When
        GameService.setGameSubscription(gameTitle, false);
        boolean result = GameService.gameInSubscription(gameTitle);

        // Then
        assertFalse(result, "The game's subscription status should be updated correctly.");
    }

    @Test
    public void testChangeGamePrice_UpdatesPriceCorrectly() {
        // Given
        String gameTitle = "The First Berserker";
        double newPrice = 49.99;

        // When
        GameService.changeGamePrice(gameTitle, newPrice);
        double updatedPrice = GameService.getGamePrice(gameTitle);

        // Then
        assertEquals(newPrice, updatedPrice, "The price should be updated correctly.");
    }
}
