package org.example.Frontend.GUIStates;

import javax.swing.*;
import java.awt.*;
/**
 * The Staff Menu GUI for managing staff actions within the Game Library Manager.
 * Allows staff to edit games, refund users, log out, or exit the application.
 */
public class StaffGUI extends JFrame {

    /**
     * Constructs the "Staff Menu" window for managing staff tasks.
     * Provides buttons for editing games, refunding users, logging out, and exiting the application.
     */

    public StaffGUI() {
        setTitle("Staff Menu - Game Library Manager");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(4, 1));

        JButton editGamesButton = new JButton("Edit Games");
        JButton refundUserButton = new JButton("Refund User");
        JButton logoutButton = new JButton("Log Out");
        JButton exitButton = new JButton("Exit");

        // Action Listeners for buttons
        editGamesButton.addActionListener(e -> openEditGamesMenu());
        refundUserButton.addActionListener(e -> openRefundUserMenu());
        logoutButton.addActionListener(e -> logout());
        exitButton.addActionListener(e -> exit());

        // Add buttons to the frame
        add(editGamesButton);
        add(refundUserButton);
        add(logoutButton);
        add(exitButton);

        setVisible(true);
    }

    /**
     * Opens the "Edit Games" menu to allow staff to modify the game catalog.
     */

    private void openEditGamesMenu() {
        this.setVisible(false);
        new EditGameGUI(this);
    }

    /**
     * Opens the "Refund User" menu to allow staff to refund a user.
     */

    private void openRefundUserMenu() {
        this.setVisible(false);
        new RefundGUI(this);
    }

    /**
     * Logs out the current staff user and returns to the main menu.
     */

    private void logout() {
        this.dispose();
        new MainMenuGUI();
    }

    /**
     * Prompts the staff for confirmation before exiting the application.
     * If confirmed, the application exits.
     */

    private void exit() {
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
