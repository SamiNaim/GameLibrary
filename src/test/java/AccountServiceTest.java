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
        // Given the email "user1@example.com" is registered in the system
        String email = "user1@example.com";

        // When the system checks if the email exists
        boolean result = AccountService.emailExists(email);

        // Then it returns true
        assertTrue(result, "The email should exist in the database.");
    }

    @Test
    public void testEmailExists_ReturnsFalse_WhenEmailIsNotFound() {
        // Given the email "nonexistent@example.com" is not registered in the system
        String email = "nonexistent@example.com";

        // When the system checks if the email exists
        boolean result = AccountService.emailExists(email);

        // Then it returns false
        assertFalse(result, "The email should not exist in the database.");
    }

    @Test
    public void testVerifyPassword_ReturnsTrue_WhenPasswordMatches() {
        // Given the email "user1@example.com" is registered with password "password123"
        String email = "user1@example.com";
        String password = "password123";

        // When the system verifies the correct password
        boolean result = AccountService.verifyPassword(email, password);

        // Then it returns true
        assertTrue(result, "The password should match the stored password.");
    }

    @Test
    public void testVerifyPassword_ReturnsFalse_WhenPasswordDoesNotMatch() {
        // Given the email "user1@example.com" is registered with password "password123"
        String email = "user1@example.com";
        String password = "wrongpassword";

        // When the system verifies an incorrect password "wrongpassword"
        boolean result = AccountService.verifyPassword(email, password);

        // Then it returns false
        assertFalse(result, "The password should not match the stored password.");
    }

    @Test
    public void testIsStaff_ReturnsFalse_WhenUserIsNotStaff() {
        // Given the user "user1@example.com" is not a staff member
        String email = "user1@example.com";
        int userID = AccountService.getUserID(email);

        // When the system checks if the user is a staff member
        boolean result = AccountService.isStaff(userID);

        // Then it returns false
        assertFalse(result, "The user should not be a staff member.");
    }

    @Test
    public void testRegisterUser_ReturnsTrue_WhenRegistrationIsSuccessful() {
        // Given the email "newuser@example.com" is not registered in the system
        String email = "newuser@example.com";
        String password = "newpassword";
        String name = "New User";
        String phoneNo = "1234567890";

        // When the system registers the user with that email
        boolean result = AccountService.registerUser(email, password, name, phoneNo);

        // Then the registration succeeds and returns true
        assertTrue(result, "The user should be successfully registered.");
    }

    @Test
    public void testRegisterUser_ReturnsFalse_WhenRegistrationFails() {
        // Given the email "user1@example.com" is already registered in the system
        String email = "user1@example.com";
        String password = "password123";
        String name = "Existing User";
        String phoneNo = "0987654321";

        // When the system attempts to register the user with the same email
        boolean result = AccountService.registerUser(email, password, name, phoneNo);

        // Then the registration fails and returns false
        assertFalse(result, "The registration should fail due to duplicate email.");
    }

    @Test
    public void testGetUserID_ReturnsValidID_WhenEmailExists() {
        // Given the email "user1@example.com" is registered in the system
        String email = "user1@example.com";

        // When the system retrieves the user ID for the email
        int userID = AccountService.getUserID(email);

        // Then it returns a valid user ID
        assertNotEquals(-1, userID, "The user ID should be valid.");
    }

    @Test
    public void testGetUserID_ReturnsInvalidID_WhenEmailDoesNotExist() {
        // Given the email "nonexistent@example.com" is not registered in the system
        String email = "nonexistent@example.com";

        // When the system retrieves the user ID for the email
        int userID = AccountService.getUserID(email);

        // Then it returns -1 indicating the user does not exist
        assertEquals(-1, userID, "The user ID should be invalid.");
    }

    @Test
    public void testGetUserBalance_ReturnsCorrectBalance() {
        // Given the user "user1@example.com" has a newly created account
        String email = "user1@example.com";
        int userID = AccountService.getUserID(email);

        // When the system checks the user's balance
        double balance = AccountService.getUserBalance(userID);

        // Then the balance is 0.0
        assertEquals(0.0, balance, "The balance should be 0.0 for the new user.");
    }

    @Test
    public void testSetUserBalance_UpdatesBalanceCorrectly() {
        // Given the user "user1@example.com" has a valid user ID
        String email = "user1@example.com";
        int userID = AccountService.getUserID(email);
        double newBalance = 100.0;

        // When the system sets the user's balance to 100.0
        AccountService.setUserBalance(userID, newBalance);
        double updatedBalance = AccountService.getUserBalance(userID);

        // Then retrieving the balance returns 100.0
        assertEquals(newBalance, updatedBalance, "The balance should be updated correctly.");
    }

    @Test
    public void testIsSubscribed_ReturnsFalse_WhenUserIsNotSubscribed() {
        // Given the user "user1@example.com" is not subscribed
        String email = "user1@example.com";
        int userID = AccountService.getUserID(email);

        // When the system checks the user's subscription status
        boolean result = AccountService.isSubscribed(userID);

        // Then it returns false
        assertFalse(result, "The user should not be subscribed.");
    }

    @Test
    public void testSetUserSubscribed_UpdatesSubscriptionStatusCorrectly() {
        // Given the user "user1@example.com" is not subscribed
        String email = "user1@example.com";
        int userID = AccountService.getUserID(email);

        // When the system sets the user's subscription status to true
        AccountService.setUserSubscribed(userID, true);
        boolean updatedStatus = AccountService.isSubscribed(userID);

        // Then checking the subscription status returns true
        assertTrue(updatedStatus, "The user's subscription status should be updated correctly.");
    }
}
