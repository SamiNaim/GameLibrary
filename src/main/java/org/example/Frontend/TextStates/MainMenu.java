package org.example.Frontend.TextStates;

import org.example.Database.AccountService;

import java.util.Optional;
import java.util.Scanner;
/**
 * The MainMenu class represents the main menu of the text-based UI where users can sign up, log in, or exit the system.
 * It handles user input for registration and login and directs the user to appropriate menus based on their role (staff or regular user).
 */
public class MainMenu implements TextUIState {
    private boolean terminated = false;

    /**
     * Handles the user's input for the main menu options.
     * Displays available options (Sign Up, Log In, Exit) and processes the user's choice.
     *
     * @param context the context of the text UI, used to change states.
     * @param scanner the scanner to read user input.
     */

    @Override
    public void handle(TextUIContext context, Scanner scanner) {
        System.out.println("""
                
                    --- Main Menu ---
                    1. Sign Up
                    2. Log In
                    3. Exit
                """);
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                addUser(scanner);
                context.changeState(new MainMenu());
            }
            case 2 -> {
                Optional<Integer> user = logIn(scanner);
                if (user.isEmpty()) {
                    context.changeState(new MainMenu());
                } else {
                    if (AccountService.isStaff(user.get())) {
                        context.changeState(new StaffMenu(user.get()));
                    } else {
                        context.changeState(new UserMenu(user.get()));
                    }
                }
            }
            case 3 -> {
                System.out.println("Goodbye!");
                terminated = true;
            }
            default -> System.out.println("Invalid option. Try again.");
        }
    }

    /**
     * Prompts the user to enter their details (email, password, name, phone number) to register a new account.
     * If the email is already in use, the user is notified.
     *
     * @param scanner the scanner to read user input.
     */

    private void addUser(Scanner scanner) {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        if (AccountService.emailExists(email)) {
            System.out.println("Email already in use. Please log in.");
            return;
        }
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phoneNo = scanner.nextLine();

        boolean reg = AccountService.registerUser(email, password, name, phoneNo);

        if (!reg) {
            System.out.println("Registration invalid. Try again.");
        } else {
            System.out.println("Registration successful!");
        }
    }

    /**
     * Prompts the user to enter their email and password to log in.
     * If the login is successful, the user's ID is returned. Otherwise, an empty result is returned.
     *
     * @param scanner the scanner to read user input.
     * @return an Optional containing the user ID if login is successful, otherwise an empty Optional.
     */

    private Optional<Integer> logIn(Scanner scanner) {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        if (!AccountService.emailExists(email)) {
            System.out.println("Invalid email. Try again.");
            return Optional.empty();
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        if (!AccountService.verifyPassword(email, password)) {
            System.out.println("Invalid password. Try again.");
            return Optional.empty();
        }

        System.out.println("Successfully logged in!");
        return Optional.of(AccountService.getUserID(email));
    }

    /**
     * Returns whether the main menu state is terminated or not.
     * The main menu is terminated when the user chooses to exit the program.
     *
     * @return true if the main menu is terminated, false otherwise.
     */

    @Override
    public boolean isTerminated() {
        return terminated;
    }
}
