package org.example.Frontend.TextStates;

import org.example.Database.AccountService;
import org.example.Database.GameService;
import org.example.Database.UserGameService;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import java.util.Scanner;
/**
 * The GameMenu class represents the game management menu in the text-based UI.
 * It allows users to view their owned games, play a game, and access games from their subscription if applicable.
 */
public class GameMenu implements TextUIState {
    private final int userID;

    /**
     * Constructs a GameMenu for a given user.
     *
     * @param userID the unique identifier of the user interacting with the game menu.
     */

    public GameMenu(int userID) {
        this.userID = userID;
    }

    /**
     * Handles the user's input for the game management menu.
     * Displays available options and processes the user's choice.
     *
     * @param context the context of the text UI, used to change states.
     * @param scanner the scanner to read user input.
     */

    @Override
    public void handle(TextUIContext context, Scanner scanner) {
        System.out.println("""
                
                    --- Game Management ---
                    1. View My Games
                    2. Play Game
                    3. Back
                """);
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                viewGames();
                context.changeState(new GameMenu(userID));
            }
            case 2 -> {
                playGame(scanner);
                context.changeState(new GameMenu(userID));
            }
            case 3 -> context.changeState(new UserMenu(userID));
            default -> System.out.println("Invalid option. Try again.");
        }
    }

    /**
     * Displays a list of games owned by the user.
     * If the user is subscribed, it also displays games available through the subscription.
     */

    private void viewGames() {
        List<String> games = UserGameService.getUserGames(userID);

        System.out.println("Game Library:");
        for (String game : games) {
            System.out.println(game);
        }

        if (AccountService.isSubscribed(userID)) {
            List<String> subscriptionGames = GameService.getSubscriptionGames();

            System.out.println("Games from Subscription:");
            for (String game : subscriptionGames) {
                System.out.println(game);
            }
        }
    }

    /**
     * Allows the user to play a game by entering the game title.
     * If the user owns the game, a random message related to the game is displayed.
     *
     * @param scanner the scanner to read user input.
     */

    private void playGame(Scanner scanner) {
        System.out.print("Enter game: ");
        String game = scanner.nextLine();

        if (!UserGameService.gameOwned(userID, game)) {
            System.out.println("Game is not owned!");
            return;
        }

        String[] options = {
                "%s was so fun!",
                "%s is hard!",
                "I should stop playing %s now.",
                "Time to stop %s and play another game!",
                "Enough %s, I need to get to my work now."
        };

        int randomIndex = ThreadLocalRandom.current().nextInt(options.length);
        String randomMessage = String.format(options[randomIndex], game);

        System.out.println(randomMessage);
    }

    /**
     * Returns whether the state is terminated or not. In this case, the game menu is not terminated.
     *
     * @return false, as the game menu is not terminated.
     */

    @Override
    public boolean isTerminated() {
        return false;
    }
}
