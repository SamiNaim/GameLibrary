package org.example.Frontend.TextStates;

import org.example.Database.GameService;
import org.example.core.Genre;

import java.util.Scanner;
/**
 * The EditGameMenu class represents the game management menu in the text-based UI.
 * Provides functionality for staff to add, remove, and modify games in the system, as well as manage subscription-related game operations.
 */
public class EditGameMenu implements TextUIState {
    private boolean terminated = false;
    private final int userID;

    /**
     * Constructs an EditGameMenu for a given user.
     *
     * @param userID the unique identifier of the staff user managing the games.
     */

    public EditGameMenu(int userID) {
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
                    1. Add Game
                    2. Remove Game
                    3. Add Game to Subscription
                    4. Remove Game from Subscription
                    5. Change Game Price
                    6. Back
                """);
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                addGame(scanner);
                context.changeState(new EditGameMenu(userID));
            }
            case 2 -> {
                removeGame(scanner);
                context.changeState(new EditGameMenu(userID));
            }
            case 3 -> {
                addGameSub(scanner);
                context.changeState(new EditGameMenu(userID));
            }
            case 4 -> {
                removeGameSub(scanner);
                context.changeState(new EditGameMenu(userID));
            }
            case 5 -> {
                changePrice(scanner);
                context.changeState(new EditGameMenu(userID));
            }
            case 6 -> context.changeState(new StaffMenu(userID));
            default -> System.out.println("Invalid option. Try again.");
        }

    }

    /**
     * Prompts the user to add a new game to the system.
     * Includes game title, developer, genre, price, copies, and subscription status.
     *
     * @param scanner the scanner to read user input.
     */

    private void addGame(Scanner scanner) {
        System.out.print("Enter game title: ");
        String title = scanner.nextLine();

        System.out.print("Enter developer: ");
        String developer = scanner.nextLine();

        System.out.print("Enter genre: ");
        String genreIn = scanner.nextLine();
        Genre genre;
        try {
            genre = Genre.valueOf(genreIn.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.print("Invalid genre.");
            return;
        }

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter copies: ");
        int copies = scanner.nextInt();

        System.out.print("Is this game part of the subscription? (yes/no): ");
        String subInput = scanner.nextLine();
        int subscription = subInput.equalsIgnoreCase("yes") ? 1 : 0;

        GameService.addGame(title, developer, genre, price, copies, subscription);
        System.out.println("Game added successfully!");
    }

    /**
     * Prompts the user to remove an existing game from the system.
     *
     * @param scanner the scanner to read user input.
     */

    private void removeGame(Scanner scanner) {
        System.out.print("Enter game title to remove: ");
        String title = scanner.nextLine();

        if (GameService.gameExists(title)) {
            GameService.removeGame(title);
            System.out.println("Game removed successfully!");
        } else {
            System.out.println("Game not found.");
        }
    }

    /**
     * Prompts the user to add an existing game to the subscription service.
     *
     * @param scanner the scanner to read user input.
     */

    private void addGameSub(Scanner scanner) {
        System.out.print("Enter game title to add to subscription: ");
        String title = scanner.nextLine();

        if (GameService.gameExists(title)) {
            GameService.setGameSubscription(title, true);
            System.out.println("Game added to subscription successfully!");
        } else {
            System.out.println("Game not found.");
        }
    }

    /**
     * Prompts the user to remove an existing game from the subscription service.
     *
     * @param scanner the scanner to read user input.
     */

    private void removeGameSub(Scanner scanner) {
        System.out.print("Enter game title to remove from subscription: ");
        String title = scanner.nextLine();

        if (GameService.gameExists(title)) {
            GameService.setGameSubscription(title, false);
            System.out.println("Game removed from subscription successfully!");
        } else {
            System.out.println("Game not found.");
        }
    }

    /**
     * Prompts the user to change the price of an existing game.
     *
     * @param scanner the scanner to read user input.
     */

    private void changePrice(Scanner scanner) {
        System.out.print("Enter game title to change price: ");
        String title = scanner.nextLine();

        System.out.print("Enter new price: ");
        double newPrice = scanner.nextDouble();

        if (GameService.gameExists(title)) {
            GameService.changeGamePrice(title, newPrice);
            System.out.println("Price updated successfully!");
        } else {
            System.out.println("Game not found.");
        }
    }

    /**
     * Returns whether the state is terminated or not. In this case, the edit game menu is not terminated.
     *
     * @return false, as the edit game menu is not terminated.
     */

    @Override
    public boolean isTerminated() {
        return terminated;
    }
}
