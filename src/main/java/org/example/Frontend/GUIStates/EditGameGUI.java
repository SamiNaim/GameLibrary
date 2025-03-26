package org.example.Frontend.GUIStates;

import org.example.Database.GameService;
import org.example.core.Genre;

import javax.swing.*;
import java.awt.*;
/**
 * A graphical user interface (GUI) for managing games in the system.
 * Allows staff members to view, add, modify, and remove games from the game catalog.
 */
public class EditGameGUI extends JFrame{
    private final JFrame staffMenu;

    private final JPanel gamePanel;

    /**
     * Constructs the EditGameGUI window for managing games.
     *
     * @param staffMenu The staff menu window to navigate back to.
     */

    public EditGameGUI(JFrame staffMenu) {
        this.staffMenu = staffMenu;

        setTitle("Game Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
        refreshGamePanel();

        JScrollPane scrollPane = new JScrollPane(gamePanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        JButton addGameButton = new JButton("Add Game");
        JButton backButton = new JButton("Back");

        addGameButton.addActionListener(e -> showAddGameDialog());
        backButton.addActionListener(e -> back());

        bottomPanel.add(addGameButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Refreshes the game panel to reflect the current list of games in the system.
     * It will update the UI to show all games with options to modify or remove them.
     */

    private void refreshGamePanel() {
        gamePanel.removeAll();
        java.util.List<String> games = GameService.getAllGames();
        for (String game : games) {
            String[] gameDetails = game.split(" - ");
            String title = gameDetails[0];
            String dev = gameDetails[1];
            String genre = gameDetails[2];
            String price = gameDetails[3];

            //JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            //panel.setBorder(BorderFactory.createTitledBorder(title));

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(1, 7));

            JLabel titleLabel = new JLabel(title);
            JLabel devLabel = new JLabel(dev);
            JLabel genreLabel = new JLabel(genre);
            JLabel priceLabel = new JLabel(price);
            JCheckBox subCheck = new JCheckBox("Subscription", GameService.gameInSubscription(title));

            JButton changePriceButton = new JButton("Change Price");
            JButton removeButton = new JButton("❌");

            changePriceButton.addActionListener(e -> changePrice(title));
            removeButton.addActionListener(e -> {
                GameService.removeGame(title);
                refreshGamePanel();
            });
            subCheck.addActionListener(e -> {
                GameService.setGameSubscription(title, subCheck.isSelected());
                refreshGamePanel();
            });

            panel.add(titleLabel);
            panel.add(devLabel);
            panel.add(genreLabel);
            panel.add(priceLabel);
            panel.add(subCheck);
            panel.add(changePriceButton);
            panel.add(removeButton);

            gamePanel.add(panel);
        }

        gamePanel.revalidate();
        gamePanel.repaint();
    }

    /**
     * Opens a dialog to change the price of a specific game.
     * It prompts the user for a new price and updates the game price accordingly.
     *
     * @param title The title of the game whose price is to be changed.
     */

    private void changePrice(String title) {
        String input = JOptionPane.showInputDialog(this, "Enter new price for " + title + ":");
        if (input != null) {
            try {
                double newPrice = Double.parseDouble(input);
                GameService.changeGamePrice(title, newPrice);
                refreshGamePanel();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid price.");
            }
        }
    }

    /**
     * Opens a dialog to add a new game to the system.
     * The user can enter the details of the game, including title, developer, genre, price, copies, and subscription status.
     */

    private void showAddGameDialog() {
        JTextField titleField = new JTextField();
        JTextField developerField = new JTextField();
        JComboBox<Genre> genreComboBox = new JComboBox<>(Genre.values());
        JTextField priceField = new JTextField();
        JTextField copiesField = new JTextField();
        JCheckBox subscriptionBox = new JCheckBox("Part of Subscription");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Title:"));
        panel.add(titleField);
        panel.add(new JLabel("Developer:"));
        panel.add(developerField);
        panel.add(new JLabel("Genre:"));
        panel.add(genreComboBox);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Copies:"));
        panel.add(copiesField);
        panel.add(subscriptionBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Game", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText();
                String dev = developerField.getText();
                Genre genre = (Genre) genreComboBox.getSelectedItem();
                double price = Double.parseDouble(priceField.getText());
                int copies = Integer.parseInt(copiesField.getText());
                int sub = subscriptionBox.isSelected() ? 1 : 0;

                GameService.addGame(title, dev, genre, price, copies, sub);
                refreshGamePanel();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please try again.");
            }
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
