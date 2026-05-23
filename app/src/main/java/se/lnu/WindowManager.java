package se.lnu;

import javafx.stage.Stage;

/**
 * Centralized window management for consistent sizing and positioning.
 * All screens should use this class to set scenes and ensure uniform dimensions.
 */
public class WindowManager {

    // Default window dimensions used when creating scenes
    public static final double WINDOW_WIDTH = 1200;
    public static final double WINDOW_HEIGHT = 800;

    // Private constructor - utility class only
    private WindowManager() {}

    /**
     * Initialize the primary stage.
     * The app opens maximized so the kiosk layout has enough space.
     * Call this once from App.start()
     */
    public static void initializeStage(Stage stage) {
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.setOnCloseRequest(e -> System.exit(0));
    }

    /**
     * Keep the app maximized when navigating between screens.
     * This prevents screens from returning to a smaller fixed size.
     */
    public static void enforceStandardSize(Stage stage) {
        stage.setResizable(true);
        stage.setMaximized(true);
    }
}