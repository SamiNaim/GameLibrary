package org.example.Frontend.TextStates;

import org.example.Database.AccountService;

import java.util.Scanner;
/**
 * The StaffMenu class represents the staff-specific menu in the text-based UI.
 * It allows staff members to perform actions such as editing games, refunding users, logging out, or exiting the system.
 */
public class StaffMenu implements TextUIState{
    private boolean terminated = false;
    private final int userID;

    /**
     * Constructs a StaffMenu instance with the given user ID.
     *
     * @param userID the ID of the staff user.
     */

    public StaffMenu(int userID) {
        this.userID = userID;
    }

    /**
     * Handles the user's input for the staff menu options.
     * Displays available options (Edit Games, Refund User, Log Out, Exit) and processes the user's choice.
     *
     * @param context the context of the text UI, used to change states.
     * @param scanner the scanner to read user input.
     */

    @Override
    public void handle(TextUIContext context, Scanner scanner) {
        System.out.println("""
                
                    --- Staff Menu ---
                    1. Edit Games
                    2. Refund User
                    3. Log Out
                    4. Exit
                """);
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> context.changeState(new EditGameMenu(userID));
            case 2 -> {
                refundUser(scanner);
                context.changeState(new StaffMenu(userID));
            }
            case 3 -> {
                System.out.println("Logged out.");
                context.changeState(new MainMenu());
            }
            case 4 -> {
                System.out.println("Goodbye!");
                terminated = true;
            }
            default -> System.out.println("Invalid option. Try again.");
        }
    }

    /**
     * Handles the refund process for a user.
     * The staff member is prompted to enter the user's email and the refund amount.
     * If the email is valid, the refund is applied to the user's balance.
     *
     * @param scanner the scanner to read user input.
     */

    private void refundUser(Scanner scanner) {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        if (AccountService.emailExists(email)) {
            System.out.println("Email already in use. Please log in.");
            return;
        }

        System.out.println("Refund amount:");
        double money = scanner.nextDouble();

        int id = AccountService.getUserID(email);
        AccountService.setUserBalance(id, AccountService.getUserBalance(id) + money);
        System.out.println("Refunded successfully!");
    }

    /**
     * Returns whether the staff menu state is terminated or not.
     * The staff menu is terminated when the user chooses to exit the program.
     *
     * @return true if the staff menu is terminated, false otherwise.
     */

    @Override
    public boolean isTerminated() {
        return terminated;
    }
}
