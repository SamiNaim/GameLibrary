package org.example.Frontend;

import org.example.Frontend.GUIStates.MainMenuGUI;
import org.example.Frontend.TextStates.TextUIContext;

import javax.swing.*;
/**
 * The Main class serves as the entry point for the Game Library Manager application.
 * It initializes and starts either the text-based user interface (TextUIContext)
 * or the graphical user interface (MainMenuGUI), depending on which UI the developer wants to run.
 *
 * This class provides the main method to launch the application.
 */
public class Main {
    /**
     * The main method that serves as the entry point to the Game Library Manager application.
     * It initializes the user interface (either text-based or graphical).
     *
     * It can run either the text-based UI or the GUI, but the current code initializes the GUI.
     * The text-based UI is commented out, and you can uncomment the code to use it instead.
     *
     * @param args Command-line arguments (not used in this program).
     */
    public static void main(String[] args) {

        // Initialize and start the text-based UI
        /*TextUIContext textUI = new TextUIContext();

        while (!textUI.isTerminated()) {
            textUI.handle();
        }*/

        // Initialize and start the GUI
        SwingUtilities.invokeLater(MainMenuGUI::new);
    }
}