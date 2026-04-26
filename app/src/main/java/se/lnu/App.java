package se.lnu;

import javafx.application.Application;
import javafx.stage.Stage;
import se.lnu.database.DatabaseInitializer;

public class App extends Application {

    public static int selectedCategoryId = -1;
    public static String selectedCategoryName = "";
    public static String orderType;

    public static void setCategory(int id, String name) {
        selectedCategoryId = id;
        selectedCategoryName = name;
        System.out.println("Selected category: " + name + " (ID: " + id + ")");
    }

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