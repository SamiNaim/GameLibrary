import org.example.Database.AccountService;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {

    @BeforeAll
    public static void setup() {
        String email = "user1@example.com";
        String password = "password123";
        String name = "Existing User";
        String phoneNo = "0987654321";
        AccountService.registerUser(email, password, name, phoneNo);
    }

    @AfterAll
    public static void cleanUp() {
        AccountService.cleanUp();
    }

    @Test
    public void testEmailExists_ReturnsTrue_WhenEmailIsFound() {
        // Given
        String email = "user1@example.com";

        // When
        boolean result = AccountService.emailExists(email);

        // Then
        assertTrue(result, "The email should exist in the database.");
    }

    @Test
    public void testEmailExists_ReturnsFalse_WhenEmailIsNotFound() {
        // Given
        String email = "nonexistent@example.com";

        // When
        boolean result = AccountService.emailExists(email);

        // Then
        assertFalse(result, "The email should not exist in the database.");
    }

    @Test
    public void testVerifyPassword_ReturnsTrue_WhenPasswordMatches() {
        // Given
        String email = "user1@example.com";
        String password = "password123";

        // When
        boolean result = AccountService.verifyPassword(email, password);

        // Then
        assertTrue(result, "The password should match the stored password.");
    }

    @Test
    public void testVerifyPassword_ReturnsFalse_WhenPasswordDoesNotMatch() {
        // Given
        String email = "user1@example.com";
        String password = "wrongpassword";

        // When
        boolean result = AccountService.verifyPassword(email, password);

        // Then
        assertFalse(result, "The password should not match the stored password.");
    }

    @Test
    public void testIsStaff_ReturnsFalse_WhenUserIsNotStaff() {
        // Given
        String email = "user1@example.com";
        int userID = AccountService.getUserID(email);

        // When
        boolean result = AccountService.isStaff(userID);

        // Then
        assertFalse(result, "The user should not be a staff member.");
    }

    @Test
    public void testRegisterUser_ReturnsTrue_WhenRegistrationIsSuccessful() {
        // Given
        String email = "newuser@example.com";
        String password = "newpassword";
        String name = "New User";
        String phoneNo = "1234567890";

        // When
        boolean result = AccountService.registerUser(email, password, name, phoneNo);

        // Then
        assertTrue(result, "The user should be successfully registered.");
    }

    @Test
    public void testRegisterUser_ReturnsFalse_WhenRegistrationFails() {
        // Given
        String email = "user1@example.com"; // Duplicate email
        String password = "password123";
        String name = "Existing User";
        String phoneNo = "0987654321";

        // When
        boolean result = AccountService.registerUser(email, password, name, phoneNo);

        // Then
        assertFalse(result, "The registration should fail due to duplicate email.");
    }

    @Test
    public void testGetUserID_ReturnsValidID_WhenEmailExists() {
        // Given
        String email = "user1@example.com";

        // When
        int userID = AccountService.getUserID(email);

        // Then
        assertNotEquals(-1, userID, "The user ID should be valid.");
    }

    @Test
    public void testGetUserID_ReturnsInvalidID_WhenEmailDoesNotExist() {
        // Given
        String email = "nonexistent@example.com";

        // When
        int userID = AccountService.getUserID(email);

        // Then
        assertEquals(-1, userID, "The user ID should be invalid.");
    }

    @Test
    public void testGetUserBalance_ReturnsCorrectBalance() {
        // Given
        String email = "user1@example.com";
        int userID = AccountService.getUserID(email);

        // When
        double balance = AccountService.getUserBalance(userID);

        // Then
        assertEquals(0.0, balance, "The balance should be 0.0 for the new user.");
    }

    @Test
    public void testSetUserBalance_UpdatesBalanceCorrectly() {
        // Given
        String email = "user1@example.com";
        int userID = AccountService.getUserID(email);
        double newBalance = 100.0;

        // When
        AccountService.setUserBalance(userID, newBalance);
        double updatedBalance = AccountService.getUserBalance(userID);

        // Then
        assertEquals(newBalance, updatedBalance, "The balance should be updated correctly.");
    }

    @Test
    public void testIsSubscribed_ReturnsFalse_WhenUserIsNotSubscribed() {
        // Given
        String email = "user1@example.com";
        int userID = AccountService.getUserID(email);

        // When
        boolean result = AccountService.isSubscribed(userID);

        // Then
        assertFalse(result, "The user should not be subscribed.");
    }

    @Test
    public void testSetUserSubscribed_UpdatesSubscriptionStatusCorrectly() {
        // Given
        String email = "user1@example.com";
        int userID = AccountService.getUserID(email);

        // When
        AccountService.setUserSubscribed(userID, true);
        boolean updatedStatus = AccountService.isSubscribed(userID);

        // Then
        assertTrue(updatedStatus, "The user's subscription status should be updated correctly.");
    }
}
