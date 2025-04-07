package org.example.Frontend.GUIStates;

import org.example.Database.AccountService;
import org.example.Frontend.TextStates.StaffMenu;

import javax.swing.*;
import java.awt.*;
/**
 * A graphical user interface (GUI) for logging in to the Game Library Manager. It allows the user to input their email and password
 * to authenticate, and navigates to the appropriate screen based on their role (staff or regular user).
 */

public class LoginGUI extends JFrame{
    private final JFrame mainMenu;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JButton login, back;
    GridBagConstraints c = new GridBagConstraints();

    /**
     * Constructs the LoginGUI window to allow the user to log in with their credentials.
     *
     * @param mainMenu The main menu frame to return to if the user chooses to go back.
     */

    public LoginGUI(JFrame mainMenu) {
        this.mainMenu = mainMenu;
        setTitle("Log In - Game Library Manager");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        JLabel emailLabel = new JLabel("Email:");
        c.gridx = 0;
        c.gridy = 0;
        add(emailLabel, c);
        emailField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 0;
        add(emailField, c);

        JLabel passwordLabel = new JLabel("Password:");
        c.gridx = 0;
        c.gridy = 1;
        add(passwordLabel, c);
        passwordField = new JPasswordField(20);
        c.gridx = 1;
        c.gridy = 1;
        add(passwordField, c);

        login = new JButton("Log In");
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        add(login, c);

        back = new JButton("Back");
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        add(back, c);

        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        add(errorLabel, c);

        login.addActionListener(e -> login());
        back.addActionListener(e -> back());

        setVisible(true);
    }

    /**
     * Handles the login process by verifying the email and password.
     * If login is successful, navigates the user to the appropriate screen based on their role.
     */

    private void login() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (AccountService.emailExists(email)) {
            if (AccountService.verifyPassword(email, password)) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                int userID = AccountService.getUserID(email);
                mainMenu.dispose();
                this.dispose();
                if (AccountService.isStaff(userID)) {
                    new StaffGUI();
                } else {
                    new GameCatalogGUI(userID);
                }
            }
        }

        errorLabel.setText("Invalid email or password. Please try again.");
    }

    /**
     * Returns to the main menu screen.
     */

    private void back() {
        this.dispose();
        mainMenu.setVisible(true);
    }

}
