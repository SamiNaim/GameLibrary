package org.example.Frontend.TextStates;

import org.example.Database.AccountService;

import java.util.Scanner;

import static org.example.core.Config.SUBSCRIPTION_PRICE;
/**
 * The AccountMenu class represents the account and subscription management menu in the text-based UI.
 * Allows users to manage their account, including adding funds, subscribing, and unsubscribing.
 */
public class AccountMenu implements TextUIState {
    private final int userID;

    /**
     * Constructs an AccountMenu for a given user.
     *
     * @param userID the unique identifier of the user whose account is being managed.
     */

    public AccountMenu(int userID) {
        this.userID = userID;
    }

    /**
     * Handles the user's input for the account management menu.
     * Displays available options and processes the user's choice.
     *
     * @param context the context of the text UI, used to change states.
     * @param scanner the scanner to read user input.
     */

    @Override
    public void handle(TextUIContext context, Scanner scanner) {
        System.out.println("""
                
                    --- Account & Subscription ---
                    1. Add Funds
                    2. Subscribe
                    3. Cancel Subscription
                    4. Back
                """);
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                addFunds(scanner);
                context.changeState(new AccountMenu(userID));
            }
            case 2 -> {
                subscribe(scanner);
                context.changeState(new AccountMenu(userID));
            }
            case 3 -> {
                unsubscribe(scanner);
                context.changeState(new AccountMenu(userID));
            }
            case 4 -> context.changeState(new UserMenu(userID));
            default -> System.out.println("Invalid option. Try again.");
        }
    }

    /**
     * Prompts the user to add funds to their account.
     * The funds are added to the user's current balance.
     *
     * @param scanner the scanner to read user input.
     */

    private void addFunds(Scanner scanner) {
        System.out.print("Money amount: ");
        double money = scanner.nextDouble();

        double currentBalance = AccountService.getUserBalance(userID);

        AccountService.setUserBalance(userID, money + currentBalance);
        System.out.print("Money added successfully!");
    }

    /**
     * Allows the user to subscribe to the service if they are not already subscribed.
     * Confirms the subscription with the user and deducts the subscription price from their balance.
     *
     * @param scanner the scanner to read user input.
     */

    private void subscribe(Scanner scanner) {
        if (AccountService.isSubscribed(userID)) {
            System.out.println("Already subscribed!");
            return;
        }

        while (true) {
            System.out.printf("Confirm purchase for %.2f€? (yes/no)", SUBSCRIPTION_PRICE);
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

        double balance = AccountService.getUserBalance(userID);

        if (SUBSCRIPTION_PRICE > balance) {
            System.out.println("Insufficient funds!");
        } else {
            AccountService.setUserBalance(userID, balance - SUBSCRIPTION_PRICE);
            AccountService.setUserSubscribed(userID, true);
            System.out.println("Subscribed successfully!");
        }
    }

    /**
     * Allows the user to unsubscribe from the service if they are currently subscribed.
     * Confirms the cancellation with the user.
     *
     * @param scanner the scanner to read user input.
     */

    private void unsubscribe(Scanner scanner) {
        if (!AccountService.isSubscribed(userID)) {
            System.out.println("Not subscribed!");
            return;
        }

        while (true) {
            System.out.println("Confirm cancellation? (yes/no)");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase("no")) {
                System.out.println("Cancellation aborted.");
                return;
            } else if (!answer.equalsIgnoreCase("yes")) {
                System.out.println("Invalid input.");
            } else {
                break;
            }
        }

        AccountService.setUserSubscribed(userID, false);
        System.out.println("Unsubscribed successfully! For any refund, please contact staff");
    }

    /**
     * Returns whether the state is terminated or not. In this case, the account menu is not terminated.
     *
     * @return false, as the account menu is not terminated.
     */

    @Override
    public boolean isTerminated() {
        return false;
    }
}
