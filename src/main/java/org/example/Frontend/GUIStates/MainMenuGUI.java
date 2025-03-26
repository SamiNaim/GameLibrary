package org.example.Frontend.GUIStates;

import javax.swing.*;
import java.awt.*;
/**
 * The main menu of the Game Library Manager application. This GUI allows users to either log in, register, or exit the application.
 */
public class MainMenuGUI extends JFrame {

    JButton logIn, register, exit;
    GridBagConstraints c = new GridBagConstraints();

    /**
     * Constructs the main menu window with buttons for login, registration, and exit.
     */

    public MainMenuGUI() {
        setTitle("Main Menu - Game Library Manager");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        logIn = new JButton("Login");
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 1;
        this.add(logIn, c);

        register = new JButton("Register");
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 1;
        this.add(register, c);

        exit = new JButton("Exit");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 0.5;
        this.add(exit, c);

        logIn.addActionListener(e -> openLoginWindow());
        register.addActionListener(e -> openRegisterWindow());
        exit.addActionListener(e -> exitProgram());

        this.setVisible(true);
    }

    /**
     * Opens the login window and hides the main menu.
     */

    private void openLoginWindow() {
        this.setVisible(false);
        new LoginGUI(this);
    }

    /**
     * Opens the registration window and hides the main menu.
     */

    private void openRegisterWindow() {
        this.setVisible(false);
        new RegisterGUI(this);
    }

    /**
     * Confirms the user's intention to exit the application and closes it if confirmed.
     */

    private void exitProgram() {
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
