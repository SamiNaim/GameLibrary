package org.example.Frontend.GUIStates;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.example.Database.AccountService;
import org.example.Database.GameService;
import org.example.Database.UserGameService;
/**
 * The GUI for displaying the user's owned games and games available through subscription.
 * It allows users to view, play games, and navigate back to the game catalog.
 */
public class MyGamesGUI extends JFrame {
    private final JFrame gameCatalog;

    private JButton backButton;
    private JPanel gamePanel;

    /**
     * Constructs the "Your Games" window for displaying owned and subscribed games.
     *
     * @param userID The ID of the user to fetch their owned and subscription games.
     * @param gameCatalog The main game catalog window to return to when the user clicks "Back".
     */

    public MyGamesGUI(int userID, JFrame gameCatalog) {
        this.gameCatalog = gameCatalog;
        setTitle("Your Games - Game Library Manager");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));

        List<String> ownedGames = UserGameService.getUserGames(userID);
        addGamesToPanel(ownedGames, true);

        if (AccountService.isSubscribed(userID)) {
            List<String> subscriptionGames = GameService.getSubscriptionGames();
            subscriptionGames.removeAll(ownedGames);
            addGamesToPanel(subscriptionGames, false);
        }

        JScrollPane scrollPane = new JScrollPane(gamePanel);
        add(scrollPane, BorderLayout.CENTER);

        backButton = new JButton("Back");
        backButton.addActionListener(e -> back());
        add(backButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Adds games to the panel, distinguishing between owned and subscription games.
     *
     * @param games The list of games to be added to the panel.
     * @param owned A boolean indicating whether the games are owned or part of the subscription.
     */

    private void addGamesToPanel(List<String> games, boolean owned) {
        for (String game : games) {
            String[] gameDetails = game.split(" - ");
            String title = gameDetails[0];
            String dev = gameDetails[1];
            String genre = gameDetails[2];

            JPanel gameInfoPanel = new JPanel();
            gameInfoPanel.setLayout(new GridLayout(1, 5));

            JLabel titleLabel = new JLabel(title);
            JLabel devLabel = new JLabel(dev);
            JLabel genreLabel = new JLabel(genre);
            JLabel ownedLabel = new JLabel(owned ? "Owned" : "Subscription");

            JButton playButton = new JButton("Play");
            playButton.addActionListener(e -> playGame(title));

            gameInfoPanel.add(titleLabel);
            gameInfoPanel.add(devLabel);
            gameInfoPanel.add(genreLabel);
            gameInfoPanel.add(ownedLabel);
            gameInfoPanel.add(playButton);

            gamePanel.add(gameInfoPanel);
        }
    }

    /**
     * Simulates playing a game and displays a random message in response.
     *
     * @param title The title of the game being "played".
     */

    private void playGame(String title) {
        String[] options = {
                "%s was so fun!",
                "%s is hard!",
                "I should stop playing %s now.",
                "Time to stop %s and play another game!",
                "Enough %s, I need to get to my work now."
        };

        int randomIndex = ThreadLocalRandom.current().nextInt(options.length);
        String randomMessage = String.format(options[randomIndex], title);

        JOptionPane.showMessageDialog(this, randomMessage);
    }

    /**
     * Closes the current window and returns to the game catalog window.
     */

    private void back() {
        this.dispose();
        gameCatalog.setVisible(true);
    }
}
