package se.lnu;

import javafx.stage.Stage;

/**
 * Centralized window management for consistent sizing and positioning.
 * All screens should use this class to set scenes and ensure uniform dimensions.
 */
public class WindowManager {

    // Fixed window dimensions
    public static final double WINDOW_WIDTH = 1024;
    public static final double WINDOW_HEIGHT = 768;

    // Private constructor - utility class only
    private WindowManager() {}

    /**
     * Initialize the primary stage with fixed dimensions and prevent resizing
     * Call this once from App.start()
     */
    public static void initializeStage(Stage stage) {
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> System.exit(0));
    }

    /**
     * Restore window to standard dimensions if it gets resized or moved
     * This ensures consistency when navigating between screens
     */
    public static void enforceStandardSize(Stage stage) {
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setResizable(false);
    }
}