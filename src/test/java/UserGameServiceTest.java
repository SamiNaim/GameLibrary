import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import org.example.Database.AccountService;
import org.example.Database.GameService;
import org.example.Database.UserGameService;
import org.example.core.Genre;

import java.util.List;

public class UserGameServiceTest {

    @BeforeAll
    public static void setup() {
        String email = "user1@example.com";
        String password = "password123";
        String name = "Existing User";
        String phoneNo = "0987654321";
        AccountService.registerUser(email, password, name, phoneNo);
        AccountService.setUserSubscribed(AccountService.getUserID("user1@example.com"), true);

        String title = "The First Berserker";
        String dev = "Neople";
        double price = 59.99;
        int copies = 100;
        int subscription = 0;
        GameService.addGame(title, dev, Genre.ACTION, price, copies, subscription);

        title = "Hollow Knight";
        dev = "Team Cherry";
        price = 9.99;
        copies = 3;
        subscription = 1;
        GameService.addGame(title, dev, Genre.ADVENTURE, price, copies, subscription);

        title = "Dark Souls";
        dev = "FromSoftware";
        price = 29.99;
        copies = 5;
        subscription = 0;
        GameService.addGame(title, dev, Genre.RPG, price, copies, subscription);
    }

    @AfterAll
    public static void cleanUp() {
        UserGameService.cleanUp();
    }

    @Test
    void testGameOwnedByUser() {
        // Given the user with email "user1@example.com"
        int userID = AccountService.getUserID("user1@example.com");

        // When & then checking if the user owns specific games
        assertFalse(UserGameService.gameOwned(userID, "The First Berserker"));
        assertTrue(UserGameService.gameOwned(userID, "Hollow Knight"));
        assertFalse(UserGameService.gameOwned(userID, "Cyberpunk"));
    }

    @Test
    void testAddGameToUser() {
        // Given the user does not own "Dark Souls"
        int userID = AccountService.getUserID("user1@example.com");
        assertFalse(UserGameService.gameOwned(userID, "Dark Souls"));

        // When the user adds "Dark Souls" to their library
        UserGameService.addGameToUser("Dark Souls", userID);

        // Then the user should now own "Dark Souls"
        assertTrue(UserGameService.gameOwned(userID, "Dark Souls"));
    }

    @Test
    void testGetUserGames() {
        // Given the user with email "user1@example.com"
        int userID = AccountService.getUserID("user1@example.com");

        // When retrieving the list of games owned by the user
        List<String> games = UserGameService.getUserGames(userID);

        // Then the list of user games should be of size 1 and contain "Dark Souls"
        assertNotNull(games);
        assertEquals(1, games.size());
        assertTrue(games.getFirst().contains("Dark Souls"));
    }
}
