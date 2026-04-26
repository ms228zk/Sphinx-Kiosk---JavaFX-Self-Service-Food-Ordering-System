package se.lnu;

import javafx.application.Application;
import javafx.stage.Stage;
import se.lnu.database.DatabaseInitializer;

public class App extends Application {

    public static String orderType;
    public static String selectedCategory;
    public static MenuItem selectedMenuItem;
    public static int selectedQuantity = 1;

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseInitializer.initialize();
            System.out.println("Database initialized");
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }

        WelcomeScreen.show(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
