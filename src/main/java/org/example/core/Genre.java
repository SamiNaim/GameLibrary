package org.example.core;
/**
 * Enumeration representing different genres of video games.
 * Each genre corresponds to a specific category of games.
 */
public enum Genre {
    ACTION, RPG, STRATEGY, SIMULATION, SPORTS, HORROR, PUZZLE, ADVENTURE, SHOOTER, PLATFORMER;

    /**
     * Returns the name of the genre in lowercase format.
     * This ensures consistency when displaying genres in a user-friendly format.
     *
     * @return The lowercase string representation of the genre.
     */
    @Override
    public String toString() {
        return name().toLowerCase(); // Converts enum names to lowercase
    }
}
