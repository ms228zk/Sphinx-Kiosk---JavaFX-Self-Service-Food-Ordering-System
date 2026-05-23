package se.lnu;

import javafx.application.Application;
import javafx.stage.Stage;
import se.lnu.database.DatabaseInitializer;

public class App extends Application {

    public static int selectedCategoryId = -1;
    public static String selectedCategoryName = "";
    public static String orderType;
    public static String selectedCategory;
    public static MenuItem selectedMenuItem;
    public static int selectedQuantity = 1;

    // Stores the payment method selected by the user.
    // This helps keep the selected payment option if the user goes back to edit the order.
    public static String selectedPaymentMethod = "";

    public static void setCategory(int id, String name) {
        selectedCategoryId = id;
        selectedCategoryName = name;
        System.out.println("Selected category: " + name + " (ID: " + id + ")");
    }

    public static void resetOrder() {
        selectedCategoryId = -1;
        selectedCategoryName = "";
        orderType = null;
        selectedCategory = null;
        selectedMenuItem = null;
        selectedQuantity = 1;
        selectedPaymentMethod = "";
        Cart.getInstance().reset();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            DatabaseInitializer.initialize();
            System.out.println("Database initialized");
        } catch (Exception e) {
            System.out.println("Database error: " + e.getMessage());
        }

        WindowManager.initializeStage(primaryStage);
        WelcomeScreen.show(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}