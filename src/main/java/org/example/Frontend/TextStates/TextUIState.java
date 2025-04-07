package org.example.Frontend.TextStates;

import java.util.Scanner;
/**
 * The TextUIState interface defines the methods that any state in the text-based user interface (UI) should implement.
 * Each state handles user input and defines its own behavior, such as transitions between states or performing specific actions.
 */
public interface TextUIState {
    /**
     * Handles the user input for the current state.
     * This method is responsible for processing the user's actions and updating the UI state accordingly.
     *
     * @param state The current context of the text-based UI. This allows the state to change to another state if needed.
     * @param scanner A Scanner instance for reading user input.
     */
    void handle(TextUIContext state, Scanner scanner);
    /**
     * Determines whether the current state is terminated.
     * If the state is terminated, the UI will stop running or transition out of the current state.
     *
     * @return true if the state is terminated, false otherwise.
     */
    boolean isTerminated();
}
