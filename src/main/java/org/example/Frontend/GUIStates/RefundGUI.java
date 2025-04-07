package org.example.Frontend.GUIStates;

import javax.swing.*;
import java.awt.*;

import org.example.Database.AccountService;
/**
 * The GUI for handling the refund process for a user.
 * Allows staff to issue refunds to users based on their email and specified refund amount.
 */
public class RefundGUI extends JFrame {
    private final JFrame staffMenu;

    private JTextField emailField, refundAmountField;

    /**
     * Constructs the "Refund User" window for issuing refunds to users.
     *
     * @param staffMenu The staff menu window to return to when the user clicks "Back".
     */

    public RefundGUI(JFrame staffMenu) {
        this.staffMenu = staffMenu;

        setTitle("Refund User - Game Library Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();

        JLabel emailLabel = new JLabel("User Email:");
        c.gridx = 0;
        c.gridy = 0;
        add(emailLabel, c);
        emailField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 0;
        add(emailField, c);

        JLabel refundAmountLabel = new JLabel("Refund Amount:");
        c.gridx = 0;
        c.gridy = 1;
        add(refundAmountLabel, c);
        refundAmountField = new JTextField(10);
        c.gridx = 1;
        c.gridy = 1;
        add(refundAmountField, c);

        JButton refundButton = new JButton("Refund");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        add(refundButton, c);

        JButton backButton = new JButton("Back");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        add(backButton, c);

        // Action Listeners
        refundButton.addActionListener(e -> refundUser());
        backButton.addActionListener(e -> back());

        setVisible(true);
    }

    /**
     * Processes the refund by verifying the user email and applying the refund amount.
     * If the email is valid, the user's balance is updated accordingly.
     */

    private void refundUser() {
        String email = emailField.getText();
        String amount = refundAmountField.getText();

        try {
            double refundAmount = Double.parseDouble(amount);
            if (AccountService.emailExists(email)) {
                int userID = AccountService.getUserID(email);
                AccountService.setUserBalance(userID, AccountService.getUserBalance(userID) + refundAmount);
                JOptionPane.showMessageDialog(this, "Refund successful!");
            } else {
                JOptionPane.showMessageDialog(this, "User not found.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid refund amount.");
        }
    }

    /**
     * Closes the current window and returns to the staff menu.
     */

    private void back() {
        this.dispose();
        staffMenu.setVisible(true);
    }
}
