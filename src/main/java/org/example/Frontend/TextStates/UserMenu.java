package org.example.Frontend.TextStates;

import org.example.Database.AccountService;
import org.example.Database.GameService;
import org.example.Database.UserGameService;

import java.util.List;
import java.util.Scanner;
/**
 * The UserMenu class represents the menu options available to a user in the text-based user interface.
 * It provides options for browsing the game catalog, purchasing games, managing owned games,
 * managing the user's account and subscription, logging out, or exiting the application.
 *
 * This class implements the {@link TextUIState} interface.
 */
public class UserMenu implements TextUIState {
    private boolean terminated = false;
    private final int userID;

    /**
     * Constructs a UserMenu instance with the specified user ID.
     *
     * @param userID The ID of the user.
     */

    public UserMenu(int userID) {
        this.userID = userID;
    }

    /**
     * Handles user input and interacts with the user based on the selected menu option.
     * It can navigate to different sections of the application, such as the game catalog,
     * game purchase process, game management, account & subscription management, logging out, or exiting.
     *
     * @param context The context of the text-based UI.
     * @param scanner A Scanner instance for reading user input.
     */

    @Override
    public void handle(TextUIContext context, Scanner scanner) {
        System.out.println("""
                
                    --- User Menu ---
                    1. Game Catalog
                    2. Purchase Game
                    3. Manage My Games
                    4. Account & Subscription
                    5. Log Out
                    6. Exit
                """);
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                gameCatalog();
                context.changeState(new UserMenu(userID));
            }
            case 2 -> {
                purchaseGame(scanner);
                context.changeState(new GameMenu(userID));
            }
            case 3 -> context.changeState(new GameMenu(userID));
            case 4 -> context.changeState(new AccountMenu(userID));
            case 5 -> {
                System.out.println("Logged out.");
                context.changeState(new MainMenu());
            }
            case 6 -> {
                System.out.println("Goodbye!");
                terminated = true;
            }
            default -> System.out.println("Invalid option. Try again.");
        }
    }

    /**
     * Displays the game catalog to the user.
     * Retrieves and prints the list of all available games.
     */

    private void gameCatalog() {
        List<String> games = GameService.getAllGames();

        System.out.println("Game Catalog:");

        for (String game : games) {
            System.out.println(game);
        }
    }

    /**
     * Handles the process of purchasing a game.
     * It ensures the game exists, is available, and the user has sufficient funds.
     * If the game is part of the subscription, it suggests the user subscribe.
     * If the user confirms the purchase, the game is added to their library, the price is deducted, and the available copies are decreased.
     *
     * @param scanner A Scanner instance for reading user input.
     */

    private void purchaseGame(Scanner scanner) {
        System.out.println("Insert a game's name to purchase:");
        String game = scanner.nextLine();

        if (!GameService.gameExists(game) || !GameService.gameAvailable(game)) {
            System.out.println("Game does not exist/is unavailable!");
        } else if (UserGameService.gameOwned(userID, game)) {
            System.out.println("Game already owned!");
        } else {
            double balance = AccountService.getUserBalance(userID);
            double gamePrice = GameService.getGamePrice(game);

            if (GameService.gameInSubscription(game)) {
                System.out.println("This game is included in our subscription. Consider subscribing and having access to this and many more games!");
            }

            while (true) {
                System.out.printf("Confirm purchase for %.2f€? (yes/no)", gamePrice);
                String answer = scanner.nextLine();

                if (answer.equalsIgnoreCase("no")) {
                    System.out.println("Purchase cancelled.");
                    return;
                } else if (!answer.equalsIgnoreCase("yes")) {
                    System.out.println("Invalid input.");
                } else {
                    break;
                }
            }

            if (gamePrice > balance) {
                System.out.println("Insufficient funds!");
            } else {
                AccountService.setUserBalance(userID, balance - gamePrice);
                UserGameService.addGameToUser(game, userID);
                GameService.decreaseCopies(game);
                System.out.println("Game purchased successfully!");
            }
        }
    }

    /**
     * Checks whether the current state is terminated.
     *
     * @return true if the state is terminated, false otherwise.
     */

    @Override
    public boolean isTerminated() {
        return terminated;
    }
}
