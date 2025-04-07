package org.example.Frontend.GUIStates;

import org.example.Database.AccountService;

import javax.swing.*;
import java.awt.*;
/**
 * The GUI for handling the registration process of a new user.
 * Allows users to create an account by providing their email, password, name, and phone number.
 */
public class RegisterGUI extends JFrame{
    private final JFrame mainMenu;

    private JTextField emailField, nameField, phoneField;
    private JPasswordField passwordField;
    private JLabel errorLabel;
    private JButton register, back;
    GridBagConstraints c = new GridBagConstraints();

    /**
     * Constructs the "Register" window for user registration.
     *
     * @param mainMenu The main menu window to return to when the user clicks "Back".
     */

    public RegisterGUI(JFrame mainMenu) {
        this.mainMenu = mainMenu;
        setTitle("Register - Game Library Manager");
        setSize(400, 300);
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

        JLabel nameLabel = new JLabel("Name:");
        c.gridx = 0;
        c.gridy = 2;
        add(nameLabel, c);
        nameField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 2;
        add(nameField, c);

        JLabel phoneLabel = new JLabel("Phone:");
        c.gridx = 0;
        c.gridy = 3;
        add(phoneLabel, c);
        phoneField = new JTextField(20);
        c.gridx = 1;
        c.gridy = 3;
        add(phoneField, c);

        register = new JButton("Register");
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        add(register, c);

        back = new JButton("Back");
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        add(back, c);

        errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setForeground(Color.RED);
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        add(errorLabel, c);

        register.addActionListener(e -> register());
        back.addActionListener(e -> back());

        setVisible(true);
    }

    /**
     * Registers the user by verifying the email and storing the user's information.
     * If the email is already taken, displays an error message.
     * If registration is successful, notifies the user and redirects to the login screen.
     */

    private void register() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String name = nameField.getText();
        String phone = phoneField.getText();

        if (AccountService.emailExists(email)) {
            errorLabel.setText("Email already exists. Please use a different email.");
        } else {
            boolean success = AccountService.registerUser(email, password, name, phone);
            if (success) {
                JOptionPane.showMessageDialog(this, "Registration successful! Please log in.");
                this.dispose();
                new LoginGUI(mainMenu); // Show login screen after successful registration
            } else {
                errorLabel.setText("Registration failed. Please try again.");
            }
        }
    }

    /**
     * Closes the current window and returns to the main menu.
     */

    private void back() {
        this.dispose();
        mainMenu.setVisible(true);
    }
}
