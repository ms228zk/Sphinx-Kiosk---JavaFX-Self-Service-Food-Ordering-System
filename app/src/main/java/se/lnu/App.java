package se.lnu;

import javafx.application.Application;
import javafx.stage.Stage;
import se.lnu.database.DatabaseInitializer;

public class App extends Application {

    // store user choice
    public static String orderType;

    @Override
    public void start(Stage primaryStage) {

        // keep database initialization
        try {
            DatabaseInitializer.initialize();
            System.out.println("Database initialized");
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }

        // show your screen (User Story 2)
        OrderTypeScreen.show(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}