package org.example.Frontend.GUIStates;

import org.example.Database.AccountService;
import org.example.Database.GameService;
import org.example.Database.UserGameService;
import org.example.Frontend.TextStates.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.util.List;
/**
 * A graphical user interface (GUI) that displays available games in the system and allows the user to buy games, view their account,
 * log out, or exit the application.
 */
public class GameCatalogGUI extends JFrame {
    private final int userID;

    private JButton myGamesButton, accountButton, logOutButton, exitButton;
    private JPanel gamesPanel;

    /**
     * Constructs the GameCatalogGUI window for displaying available games and options.
     *
     * @param userID The ID of the current user.
     */

    public GameCatalogGUI(int userID) {
        this.userID = userID;
        setTitle("Available Games - Game Library Manager");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        gamesPanel = new JPanel();
        gamesPanel.setLayout(new BoxLayout(gamesPanel, BoxLayout.Y_AXIS));

        List<String> games = GameService.getAllGames();
        for (String game : games) {
            String[] gameDetails = game.split(" - ");
            String title = gameDetails[0];
            String dev = gameDetails[1];
            String genre = gameDetails[2];
            String price = gameDetails[3];
            String copies = gameDetails[4];

            addGameToPanel(title, dev, genre, price, copies);
        }

        JScrollPane scrollPane = new JScrollPane(gamesPanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(1, 4));

        myGamesButton = new JButton("My Games");
        accountButton = new JButton("Account");
        logOutButton = new JButton("Log Out");
        exitButton = new JButton("Exit");

        bottomPanel.add(myGamesButton);
        bottomPanel.add(accountButton);
        bottomPanel.add(logOutButton);
        bottomPanel.add(exitButton);

        add(bottomPanel, BorderLayout.SOUTH);

        myGamesButton.addActionListener(e -> showMyGames());
        accountButton.addActionListener(e -> showAccount());
        logOutButton.addActionListener(e -> logout());
        exitButton.addActionListener(e -> exit());

        setVisible(true);
    }

    /**
     * Adds a game to the panel with its details and a "Buy" button.
     *
     * @param title   The title of the game.
     * @param dev     The developer of the game.
     * @param genre   The genre of the game.
     * @param price   The price of the game.
     * @param copies  The available copies of the game.
     */

    private void addGameToPanel(String title, String dev, String genre, String price, String copies) {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(1, 6));

        JLabel titleLabel = new JLabel(title);
        JLabel devLabel = new JLabel(dev);
        JLabel genreLabel = new JLabel(genre);
        JLabel copiesLabel = new JLabel(copies);
        JLabel priceLabel = new JLabel(price);

        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(e -> buyGame(title));

        gamePanel.add(titleLabel);
        gamePanel.add(devLabel);
        gamePanel.add(genreLabel);
        gamePanel.add(copiesLabel);
        gamePanel.add(priceLabel);
        gamePanel.add(buyButton);

        gamesPanel.add(gamePanel);
    }

    /**
     * Initiates the process of buying a game.
     * Checks for availability, ownership, and funds before allowing the purchase.
     *
     * @param gameTitle The title of the game to be purchased.
     */

    private void buyGame(String gameTitle) {
        if (!GameService.gameExists(gameTitle) || !GameService.gameAvailable(gameTitle)) {
            JOptionPane.showMessageDialog(this, "Game does not exist/is unavailable!");
        } else if (UserGameService.gameOwned(userID, gameTitle)) {
            JOptionPane.showMessageDialog(this, "Game already owned!");
        } else {
            double balance = AccountService.getUserBalance(userID);
            double gamePrice = GameService.getGamePrice(gameTitle);

            if (GameService.gameInSubscription(gameTitle)) {
                JOptionPane.showMessageDialog(this, "This game is included in our subscription. Consider subscribing and having access to this and many more games!");
            }

            int response = JOptionPane.showConfirmDialog(this, "Do you want to buy the game: " + gameTitle + "?", "Confirm Purchase", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                if (gamePrice > balance) {
                    JOptionPane.showMessageDialog(this, "Insufficient funds!");
                } else {
                    AccountService.setUserBalance(userID, balance - gamePrice);
                    UserGameService.addGameToUser(gameTitle, userID);
                    GameService.decreaseCopies(gameTitle);
                    JOptionPane.showMessageDialog(this, "Game purchased successfully!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Game not purchased.");
            }
        }
    }

    /**
     * Navigates to the user's list of owned games.
     */

    private void showMyGames() {
        this.setVisible(false);
        new MyGamesGUI(userID, this);
    }

    /**
     * Navigates to the user's account settings.
     */

    private void showAccount() {
        this.setVisible(false);
        new AccountGUI(userID, this);
    }

    /**
     * Logs out the user and returns to the main menu.
     */

    private void logout() {
        this.dispose();
        new MainMenuGUI();
    }

    /**
     * Exits the application after confirming with the user.
     */

    private void exit() {
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
