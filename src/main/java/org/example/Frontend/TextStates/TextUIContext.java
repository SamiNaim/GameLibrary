package org.example.Frontend.TextStates;

import java.util.Scanner;
/**
 * The TextUIContext class manages the current state of the text-based user interface (UI).
 * It handles the transitions between different states and serves as the main controller for the user's interaction with the system.
 */
public class TextUIContext {
    private TextUIState currentState;
    private final Scanner scanner;

    /**
     * Constructs a TextUIContext instance and initializes the scanner for user input.
     * The initial state is set to the main menu of the application.
     */

    public TextUIContext() {
        this.scanner = new Scanner(System.in);
        currentState = new MainMenu();

        System.out.println("Welcome to the Game Library Manager!");
    }

    /**
     * Executes the current state's handling method, which processes the user's input and performs the required action.
     * This method calls the handle() method of the current state, where the state-specific actions take place.
     */

    public void handle() {
        currentState.handle(this, scanner);
    }

    /**
     * Changes the current state of the UI.
     *
     * @param state the new state to transition to.
     */

    public void changeState(TextUIState state) {
        currentState = state;
    }

    /**
     * Checks if the current state is terminated. If the state is terminated, the UI should stop running.
     *
     * @return true if the current state is terminated, false otherwise.
     */

    public boolean isTerminated() {
        return currentState.isTerminated();
    }
}
