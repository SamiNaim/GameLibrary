package org.example.Frontend.GUIStates;

import javax.swing.*;
import java.awt.*;

import org.example.Database.AccountService;
import static org.example.core.Config.SUBSCRIPTION_PRICE;
/**
 * A graphical user interface (GUI) that manages user account details and subscription status.
 * Allows users to add funds, subscribe, unsubscribe, and view their balance.
 */
public class AccountGUI extends JFrame {
    private final int userID;
    private final JFrame gameCatalog;

    private JButton addFundsButton, subscribeButton, unsubscribeButton, backButton;
    private JLabel balanceLabel;

    /**
     * Constructs an AccountGUI object for managing user account information and subscription.
     *
     * @param userID     The unique ID of the user whose account is being managed.
     * @param gameCatalog The main game catalog window to navigate back to.
     */

    public AccountGUI(int userID, JFrame gameCatalog) {
        this.userID = userID;
        this.gameCatalog = gameCatalog;

        setTitle("Account & Subscription - Game Library Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        balanceLabel = new JLabel("Balance: " + String.format("%.2f", AccountService.getUserBalance(userID)) + "€");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(10, 10, 10, 10);
        add(balanceLabel, c);

        addFundsButton = new JButton("Add Funds");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        add(addFundsButton, c);
        addFundsButton.addActionListener(e -> addFunds());

        subscribeButton = new JButton("Subscribe");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        add(subscribeButton, c);
        subscribeButton.addActionListener(e -> subscribe());

        unsubscribeButton = new JButton("Cancel Subscription");
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        add(unsubscribeButton, c);
        unsubscribeButton.addActionListener(e -> unsubscribe());

        backButton = new JButton("Back");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        add(backButton, c);
        backButton.addActionListener(e -> back());

        setVisible(true);
    }

    /**
     * Opens a dialog to add funds to the user's balance.
     * The user can input an amount to add to their current balance.
     */

    private void addFunds() {
        String amountStr = JOptionPane.showInputDialog(this, "Enter amount to add:");
        if (amountStr != null) {
            try {
                double amount = Double.parseDouble(amountStr);
                double currentBalance = AccountService.getUserBalance(userID);
                AccountService.setUserBalance(userID, currentBalance + amount);
                JOptionPane.showMessageDialog(this, "Money added successfully!");
                balanceLabel.setText("Balance: " + String.format("%.2f", currentBalance + amount) + "€");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount. Please enter a valid number.");
            }
        }
    }

    /**
     * Initiates a subscription process where the user can subscribe to the service.
     * It checks if the user has sufficient funds and updates their subscription status.
     */

    private void subscribe() {
        if (AccountService.isSubscribed(userID)) {
            JOptionPane.showMessageDialog(this, "Already subscribed!");
            return;
        }

        int response = JOptionPane.showConfirmDialog(this,
                "Confirm subscription purchase for " + SUBSCRIPTION_PRICE + "€?",
                "Subscription", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            double balance = AccountService.getUserBalance(userID);
            if (balance >= SUBSCRIPTION_PRICE) {
                AccountService.setUserBalance(userID, balance - SUBSCRIPTION_PRICE);
                AccountService.setUserSubscribed(userID, true);
                JOptionPane.showMessageDialog(this, "Subscribed successfully!");
                balanceLabel.setText("Balance: " + String.format("%.2f", balance - SUBSCRIPTION_PRICE) + "€");
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient funds!");
            }
        }
    }

    /**
     * Cancels the user's subscription if they are currently subscribed.
     * A confirmation dialog is shown before the subscription is canceled.
     */

    private void unsubscribe() {
        if (!AccountService.isSubscribed(userID)) {
            JOptionPane.showMessageDialog(this, "You are not subscribed.");
            return;
        }

        int response = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to cancel the subscription?",
                "Cancel Subscription", JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            AccountService.setUserSubscribed(userID, false);
            JOptionPane.showMessageDialog(this, "Unsubscribed successfully!\nFor any refund, please contact staff.");
        }
    }

    /**
     * Closes the current window and returns to the game catalog.
     */

    private void back() {
        this.dispose();
        gameCatalog.setVisible(true);
    }
}
